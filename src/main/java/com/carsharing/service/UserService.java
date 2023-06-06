package com.carsharing.service;

import com.carsharing.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    User findById(Long id);

    Optional<User> findByEmail(String email);

    void delete(Long id);

    List<User> getAllUsers();

    List<User> findUserByRole(User.Role role);
}
