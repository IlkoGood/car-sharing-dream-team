package com.carsharing.security;

import com.carsharing.dto.request.UserLoginDto;
import com.carsharing.model.User;

public interface AuthService {
    String login(UserLoginDto loginDto);

    User register(String email, String password);
}
