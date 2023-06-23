package com.carsharing.dto.request;

import com.carsharing.model.Car;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarRequestDto {
    @NotEmpty
    @NotNull
    private String model;
    @NotEmpty
    @NotNull
    private String brand;
    @NotNull
    private Car.Type type;
    @PositiveOrZero
    @NotNull
    private int inventory;
    @DecimalMin(value = "0.01")
    @NotNull
    private BigDecimal dailyFee;
}
