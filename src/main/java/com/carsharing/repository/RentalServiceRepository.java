package com.carsharing.repository;

import com.carsharing.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalServiceRepository extends JpaRepository<Rental, Long> {
}
