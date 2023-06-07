package com.carsharing.service.impl;

import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.repository.RentalRepository;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
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
    public Rental getById(Long id) {
        return rentalRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Can`t find rental by id: " + id)
        );
    }

    @Override
    public List<Rental> getByParam(Long userId, Boolean isActive) {
        List<Rental> rentals = userId != null ? rentalRepository.findRentalsByUser_Id(userId)
                : rentalRepository.findAll();
        if (isActive != null) {
            rentals = rentals.stream()
                    .filter(r -> (r.getActualReturnDate() == null) == isActive)
                    .collect(Collectors.toList());
        }
        return rentals;
    }

    @Override
    public void createRental(Rental rental) {
        Car car = carService.getById(rental.getCar().getId());
        if (car.getInventory() < 1) {
            throw new RuntimeException("There are no available cars of this model ["
                    + car + ']');
        }
        car.setInventory(car.getInventory() - 1);
        carService.update(car);
    }

    @Override
    public Rental closeRental(Rental rental) {
        if (rental.getActualReturnDate() != null) {
            throw new RuntimeException("Rental with id: '"
                    + rental.getId() + "' is no longer active!");
        }
        rental.setActualReturnDate(LocalDateTime.now());
        Car car = carService.getById(rental.getCar().getId());
        car.setInventory(car.getInventory() + 1);
        carService.update(car);
        return rental;
    }
}
