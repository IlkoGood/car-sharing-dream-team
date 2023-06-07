package com.carsharing.security;

import org.springframework.security.core.Authentication;

public interface AccessService {
    void checkUserAccess(Authentication authentication, Long userId);

    Boolean isManager(Authentication authentication);
}
