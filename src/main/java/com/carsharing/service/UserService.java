package com.carsharing.service;

import com.carsharing.model.User;

public interface UserService {
    User save(User user);

    User findById(Long id);

    User findByEmail(String email);
}
