package com.docuguard.auth_service.service;

import com.docuguard.auth_service.entities.User;

import java.util.List;

public interface UserService {
    public List<User> getUsers();
    public User createUser(User user);
}
