package com.carsharing.dto.User;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String password;
    private String repeatPassword;
}
