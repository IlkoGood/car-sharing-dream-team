package com.carsharing.controller;

import com.carsharing.dto.User.JWTAuthResponse;
import com.carsharing.dto.User.LoginDto;
import com.carsharing.dto.User.UserRegistrationDto;
import com.carsharing.dto.User.UserResponseDto;
import com.carsharing.model.User;
import com.carsharing.security.AuthService;
import com.carsharing.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    private AuthService authService;
    private UserMapper userMapper;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRegistrationDto userRequestDto) {
        User user = authService.register(userRequestDto.getEmail(),
                userRequestDto.getPassword());
        return userMapper.mapToDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticate(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}
