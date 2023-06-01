package com.carsharing.service;

import com.carsharing.model.Rental;
import java.util.List;

public interface RentalService {
    Rental save(Rental rental);

    void delete(Rental rental);

    List<Rental> getAll();

    List<Rental> getAllByUserId(Long userId);

    List<Rental> getActive(Boolean isActive);

    Rental getById(Long id);
}
