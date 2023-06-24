package com.carsharing.dto.request;

import com.carsharing.validation.FieldsValueMatch;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
@Data
public final class UserRegistrationDto {
    @NotNull
    @Size(min = 6, max = 255)
    private String email;
    @NotNull
    @Size(min = 8, max = 40)
    private String password;
    @NotNull
    private String repeatPassword;
}
