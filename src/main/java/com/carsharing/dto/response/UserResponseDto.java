package com.carsharing.dto.response;

import lombok.Data;

@Data
public final class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
