package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.UserMapper;
import com.carsharing.dto.request.UserRequestDto;
import com.carsharing.dto.response.UserResponseDto;
import com.carsharing.exception.DataProcessingException;
import com.carsharing.model.User;
import com.carsharing.security.jwt.JwtTokenProvider;
import com.carsharing.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public UserResponseDto getUserInfo(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());
        User user = userService.findByEmail(jwtTokenProvider.getUsername(token))
                .orElseThrow(() -> new DataProcessingException("User data had been violently "
                        + "changed between authentication and request processing"));
        return userMapper.mapToDto(user);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public UserResponseDto updateUser(@RequestBody @Valid UserRequestDto requestDto,
                                      HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring("Bearer ".length());
        User user = userService.findByEmail(jwtTokenProvider.getUsername(token))
                .orElseThrow(() -> new DataProcessingException("User data had been violently "
                        + "changed between authentication and request processing"));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user = userService.save(user);
        return userMapper.mapToDto(user);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public UserResponseDto getInfoNoAuth(@PathVariable Long id, @RequestParam User.Role role) {
        User user = userService.findById(id);
        user.setRole(role);
        user = userService.save(user);
        return userMapper.mapToDto(user);
    }
}