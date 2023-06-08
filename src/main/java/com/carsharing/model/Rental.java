package com.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(name = "rental_start_date")
    private LocalDateTime rentalDate;
    @Column(name = "rental_return_date")
    private LocalDateTime returnDate;
    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;
    @Column(name = "car_id")
    private Long carId;
    @Column(name = "user_id")
    private Long userId;
}
