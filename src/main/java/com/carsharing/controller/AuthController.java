package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.UserMapper;
import com.carsharing.dto.request.UserLoginDto;
import com.carsharing.dto.request.UserRegistrationDto;
import com.carsharing.dto.response.JwtAuthResponse;
import com.carsharing.dto.response.UserResponseDto;
import com.carsharing.exception.AuthenticationException;
import com.carsharing.model.User;
import com.carsharing.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping
public class AuthController {
    private AuthService authService;
    private UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "Data for registration", description = "This endpoint allows users"
            + " to register a new account")
    public UserResponseDto register(@Parameter(schema = @Schema(type = "String",
            defaultValue = """
                    {
                        "email":"alice@gmail.com",\s
                        "password":"alice12345",\s
                        "repeatPassword":"alice12345"
                    }"""))@RequestBody @Valid UserRegistrationDto userRequestDto) {
        User user = authService.register(userRequestDto.getEmail(), userRequestDto.getPassword());
        return userMapper.mapToDto(user);
    }

    @PostMapping("/login")
    @Operation(summary = "User authentication",
            description = "Authenticates a user and returns an access token")
    public ResponseEntity<JwtAuthResponse> authenticate(@Parameter(schema = @Schema(
            type = "String", defaultValue = """
            {
                "email":"admin@gmail.com",
                "password":"admin12345"
            }"""))@RequestBody @Valid UserLoginDto loginDto)
            throws AuthenticationException {
        String token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
