package com.carsharing.service;

import com.carsharing.model.Rental;
import java.util.List;

public interface RentalService {
    Rental save(Rental rental);

    void delete(Rental rental);

    Rental getById(Long id);

    List<Rental> getByParam(Long userId, Boolean isActive);

    void createRental(Rental rental);

    Rental closeRental(Rental rental);
}
