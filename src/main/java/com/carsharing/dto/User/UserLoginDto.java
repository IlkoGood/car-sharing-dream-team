package com.carsharing.dto.User;

import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String password;
}
