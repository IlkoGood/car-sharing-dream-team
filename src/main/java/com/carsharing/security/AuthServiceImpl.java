package com.carsharing.security;

import com.carsharing.dto.user.UserLoginDto;
import com.carsharing.model.User;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(UserLoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(User.Role.CUSTOMER);
        return userService.save(user);
    }
}
