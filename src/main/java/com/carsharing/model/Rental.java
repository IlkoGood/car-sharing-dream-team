package com.carsharing.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
