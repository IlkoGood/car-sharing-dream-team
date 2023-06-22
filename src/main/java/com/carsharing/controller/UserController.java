package com.carsharing.controller;

import com.carsharing.dto.mapper.impl.UserMapper;
import com.carsharing.dto.request.UserRequestDto;
import com.carsharing.dto.response.UserResponseDto;
import com.carsharing.model.User;
import com.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get current user info",
            description = "Retrieve the information of the current user")
    public UserResponseDto getUserInfo(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return userMapper.mapToDto(user);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update current user info",
            description = "Modify the user's personal details")
    public UserResponseDto updateUser(@Parameter(schema = @Schema(type = "String",
            defaultValue = """
                    {
                        "email":"admin@gmail.com",\s
                        "password":"admin12345",\s
                        "repeatPassword":"admin12345",\s
                        "firstName":"Alice",\s
                        "lastName":"Jhonson"
                    }""")) @RequestBody @Valid UserRequestDto requestDto,
                                      Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user = userService.save(user);
        return userMapper.mapToDto(user);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Update user role",
            description = "Modify the role of a user in the system")
    public UserResponseDto getInfoNoAuth(@Parameter(description = "User id",
            example = "1") @PathVariable Long id,
                                         @Parameter(description = "User role : CUSTOMER, MANAGER",
                                                 schema = @Schema(type = "string",
                                                         defaultValue = "CUSTOMER"))
                                         @RequestParam User.Role role) {
        User user = userService.findById(id);
        user.setRole(role);
        user = userService.save(user);
        return userMapper.mapToDto(user);
    }
}
