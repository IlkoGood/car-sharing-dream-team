package com.carsharing.security;

import com.carsharing.exception.AuthenticationException;
import com.carsharing.model.User;

public interface AuthService {
    String login(String email, String password) throws AuthenticationException;

    User register(String email, String password);
}
