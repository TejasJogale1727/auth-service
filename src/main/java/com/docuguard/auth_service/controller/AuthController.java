package com.docuguard.auth_service.controller;

import com.docuguard.auth_service.dto.JWTRequest;
import com.docuguard.auth_service.dto.JWTResponse;
import com.docuguard.auth_service.entities.User;
import com.docuguard.auth_service.entities.UserAuth;
import com.docuguard.auth_service.message.MessageStatus;
import com.docuguard.auth_service.repository.UserAuthRepository;
import com.docuguard.auth_service.repository.UserRepository;
import com.docuguard.auth_service.service.UserService;
import com.docuguard.auth_service.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil helper;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/validate-token")
    public Boolean validateToken(@RequestParam(value = "token", required = true) String token) {
        String username = null;
        Boolean validateToken = false;
        User user = null;
        try {
            username = this.helper.getUsernameFromToken(token);
        } catch (SignatureException se) {
//			se.printStackTrace();
            logger.error("Invalid Token !!!");
            logger.error(se.getMessage());
        } catch (ExpiredJwtException ee) {
//			se.printStackTrace();
            logger.error("Token Expired !!!");
            logger.error(ee.getMessage());
        }
        if (username != null) {
            user = this.userRepository.findByName(username).orElse(null);
            if (user != null) {
                logger.info("Valid token !!!");
                validateToken = this.helper.validateToken(token, user);
            }
        }
        return validateToken;
    }

    @PostMapping("/login")
    public ResponseEntity<MessageStatus<JWTResponse>> login(@RequestBody JWTRequest request, HttpServletRequest req) {
        MessageStatus<JWTResponse> msg = new MessageStatus<JWTResponse>();


        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            msg.setStatusCode(HttpStatus.NOT_FOUND);
            msg.setMessage("Email address not found in our records. Please provide correct Email Id.");
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        String token = this.helper.generateToken(user);
        UserAuth userAuth = new UserAuth();
        userAuth.setUserId(user.getId());
        userAuth.setEmail(user.getEmail());
        userAuth.setName(user.getName());
        userAuth.setTokenId(token);
        userAuth.setLoginTime(new Date());
        userAuth.setExpiredTime(getExpiredTime(0,24));
        userAuth.setLogin_useragent(req.getHeader("User-Agent"));
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }
        userAuth.setLoginip(ipAddress);
        userAuthRepository.save(userAuth); // Create login entry in tbl_auth

        logger.info("User "+user.getName()+" has beeen authenticated.");
        JWTResponse response = new JWTResponse(token, user.getName());
        msg.setStatusCode(HttpStatus.OK);
        msg.setMessage("Login successfull.");
        msg.setData(response);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    public Date getExpiredTime(int minute,int hour) {
        Calendar now = Calendar.getInstance();
        if(minute >0) now.add(Calendar.MINUTE, minute);
        if(hour >0) now.add(Calendar.HOUR, hour);
        return now.getTime();
    }
}
