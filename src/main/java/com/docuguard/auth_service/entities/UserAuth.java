package com.docuguard.auth_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name="tbl_user_auth")
public class UserAuth {
    @Id
    private String tokenId;
    private String userId;
    private Date loginTime;
    private Date expiredTime;
    private String loginip;
    private String login_useragent;
    private String name;
    private String email;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public String getLogin_useragent() {
        return login_useragent;
    }

    public void setLogin_useragent(String login_useragent) {
        this.login_useragent = login_useragent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserAuth{" +
                "tokenId='" + tokenId + '\'' +
                ", userId='" + userId + '\'' +
                ", loginTime=" + loginTime +
                ", expiredTime=" + expiredTime +
                ", loginip='" + loginip + '\'' +
                ", login_useragent='" + login_useragent + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
