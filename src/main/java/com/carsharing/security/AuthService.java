package com.carsharing.security;

import com.carsharing.dto.user.LoginDto;
import com.carsharing.model.User;

public interface AuthService {
    String login(LoginDto loginDto);

    User register(String email, String password);
}
