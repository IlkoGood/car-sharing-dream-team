package com.carsharing.controller;

import com.carsharing.dto.mapper.UserMapper;
import com.carsharing.dto.user.UserResponseDto;
import com.carsharing.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public UserResponseDto getUserInfo() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(77L);
        userResponseDto.setEmail("AUTH WORKS");
        return userResponseDto;
    }
}
