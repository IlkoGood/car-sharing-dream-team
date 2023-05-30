package com.carsharing.dto.response;

import com.carsharing.model.Car;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarResponseDto {
    private Long id;
    private String model;
    private String brand;
    private Car.Type type;
    private int inventory;
    private BigDecimal dailyFee;
}
