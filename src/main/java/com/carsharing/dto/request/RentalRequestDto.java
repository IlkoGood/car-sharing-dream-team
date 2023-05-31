package com.carsharing.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class RentalRequestDto {
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private Long carId;
    private Long userId;
}
