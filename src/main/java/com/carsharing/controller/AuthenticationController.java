package com.carsharing.controller;

import com.carsharing.dto.User.UserRegistrationDto;
import com.carsharing.dto.User.UserResponseDto;
import com.carsharing.model.User;
import com.carsharing.security.AuthenticationService;
import com.carsharing.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    //private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRegistrationDto userRequestDto) {
        User user = authenticationService.register(userRequestDto.getEmail(),
                userRequestDto.getPassword());
        return userMapper.mapToDto(user);
    }

    /*@PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto userLoginDto)
            throws AuthenticationException {
        User user = authenticationService.login(
                userLoginDto.getEmail(), userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(),
                user.getRole().name());
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }*/
}
