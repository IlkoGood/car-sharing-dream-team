package com.carsharing.security;

import com.carsharing.model.User;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    //private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(User.Role.CUSTOMER);
        return userService.save(user);
    }

    /*@Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new AuthenticationException("Incorrect username or password!");
        }
        return user.get();
    }*/
}
