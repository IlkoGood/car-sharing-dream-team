package com.carsharing.dto.request;

import com.carsharing.lib.FieldsValueMatch;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Data
public class UserRegistrationDto {
    @NotNull
    @Size(min = 6, max = 255)
    private String email;
    @NotNull
    @Size(min = 8, max = 40)
    private String password;
    private String repeatPassword;
}
