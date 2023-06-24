package com.carsharing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class UserLoginDto {
    @NotNull
    @Size(min = 6, max = 255)
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 40)
    private String password;
}
