package com.carsharing.service;

import com.carsharing.model.User;
import java.util.List;

public interface UserService {
    User save(User user);

    User findById(Long id);

    User findByEmail(String email);

    void delete(Long id);

    List<User> getAllUsers();

    List<User> findUserByRole(User.Role role);
}
