package com.carsharing.dto.request;

import com.carsharing.model.Car;
import com.carsharing.validation.IsEnum;
import com.carsharing.validation.IsNumber;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public final class CarRequestDto {
    @NotNull
    @NotEmpty
    private String model;
    @NotNull
    @NotEmpty
    private String brand;
    @NotNull
    @IsEnum(enumClazz = Car.Type.class, message = "must be like [HATCHBACK, SEDAN, SUV, UNIVERSAL]")
    private String type;
    @NotNull
    @IsNumber
    @Positive
    private String inventory;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal dailyFee;
}
