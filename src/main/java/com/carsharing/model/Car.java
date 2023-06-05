package com.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String brand;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "type",
            columnDefinition = "ENUM('HATCHBACK','SEDAN','SUV','UNIVERSAL')",
            nullable = false)
    private Type type;
    private int inventory;
    @Column(name = "daily_fee", nullable = false)
    private BigDecimal dailyFee;

    public enum Type {
        HATCHBACK,
        SEDAN,
        SUV,
        UNIVERSAL
    }
}
