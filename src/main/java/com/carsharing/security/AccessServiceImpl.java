package com.carsharing.security;

import com.carsharing.service.UserService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccessServiceImpl implements AccessService {
    private static final String MANAGER = "MANAGER";
    private final UserService userService;

    @Override
    public Boolean checkUserAccess(Authentication authentication, Long userId) {
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        return !this.isManager(authentication) && !Objects.equals(authUserId, userId);
    }

    @Override
    public Boolean isManager(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(MANAGER));
    }
}
