package com.carsharing.service.impl;

import com.carsharing.model.Rental;
import com.carsharing.repository.RentalRepository;
import com.carsharing.service.RentalService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final TelegramNotificationService telegramNotificationService;

    @Override
    public Rental save(Rental rental) {
        Rental savedRental = rentalRepository.save(rental);
        telegramNotificationService.sendNotification(savedRental);
        return savedRental;
    }

    @Override
    public void delete(Rental rental) {
        rentalRepository.delete(rental);
    }

    @Override
    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> getAllByUserId(Long userId) {
        return rentalRepository.findRentalsByUser_Id(userId);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find rental by id: " + id)
        );
    }
}
