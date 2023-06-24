package com.carsharing.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public final class RentalResponseDto {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private Long carId;
    private Long userId;
}
