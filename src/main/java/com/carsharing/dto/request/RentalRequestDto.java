package com.carsharing.dto.request;

import com.carsharing.validation.IsDate;
import com.carsharing.validation.IsNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public final class RentalRequestDto {
    @NotNull
    @IsDate
    private String rentalDate;
    @NotNull
    @IsDate
    private String returnDate;
    private LocalDateTime actualReturnDate;
    @NotNull
    @IsNumber
    @Positive
    private String carId;
    @NotNull
    @IsNumber
    @Positive
    private String userId;
}
