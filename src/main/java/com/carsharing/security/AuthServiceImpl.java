package com.carsharing.security;

import com.carsharing.exception.AuthenticationException;
import com.carsharing.model.User;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String email, String password) throws AuthenticationException {
        User user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenProvider.generateToken(email);
        }
        throw new AuthenticationException("Incorrect username or password!");
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.CUSTOMER);
        return userService.save(user);
    }
}
