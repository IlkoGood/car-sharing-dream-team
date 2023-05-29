package com.carsharing.service.impl;

import com.carsharing.exception.DataProcessingException;
import com.carsharing.model.User;
import com.carsharing.repository.UserRepository;
import com.carsharing.service.UserService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new DataProcessingException("No user found for id: " + id);
        }
        return userOptional.get();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
