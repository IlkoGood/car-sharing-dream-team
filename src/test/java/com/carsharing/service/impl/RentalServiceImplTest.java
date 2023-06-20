package com.carsharing.service.impl;

import static org.mockito.Mockito.times;

import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.repository.RentalRepository;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.naming.NamingSecurityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {
    @InjectMocks
    private RentalServiceImpl rentalService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarService carService;
    @Mock
    private TelegramNotificationService telegramNotificationService;

    @Test
    void save_Ok() {
        Rental expected = getRental();
        Mockito.when(rentalRepository.save(expected)).thenReturn(expected);
        Rental actual = rentalService.save(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramNotificationService, times(1)).sendNotification(actual);
    }

    @Test
    void getById_Ok() {
        Long id = 1L;
        Rental expected = getRental();
        Mockito.when(rentalRepository.findById(id)).thenReturn(Optional.of(expected));
        Rental actual = rentalService.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById_throwsException() {
        Long id = 1L;
        Mockito.when(rentalRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> rentalService.getById(id));
    }

    @Test
    void getByParam_allParamExist_Ok() {
        Long userId = 1L;
        Boolean isActive = true;
        List<Rental> expected = getRentals(isActive, 5);
        Mockito.when(rentalRepository.findRentalsByUserId(userId)).thenReturn(expected);
        List<Rental> actual = rentalService.getByParam(userId, isActive);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByParam_nullsParam_Ok() {
        Long userId = null;
        Boolean isActive = null;
        List<Rental> expected = getRentals(false, 5);
        Mockito.when(rentalRepository.findAll()).thenReturn(expected);
        List<Rental> actual = rentalService.getByParam(userId, isActive);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByParam_userIdIsNullAndIsActiveIsFalse_Ok() {
        Long userId = null;
        Boolean isActive = false;
        List<Rental> expected = getRentals(isActive, 5);
        expected.get(0).setActualReturnDate(null);
        expected.get(2).setActualReturnDate(null);
        expected.remove(0);
        expected.remove(1);
        Mockito.when(rentalRepository.findAll()).thenReturn(expected);
        List<Rental> actual = rentalService.getByParam(userId, isActive);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByParam_userIdIsNullAndIsActiveIsTrue_Ok() {
        Long userId = null;
        Boolean isActive = true;
        List<Rental> expected = getRentals(isActive, 5);
        expected.get(0).setActualReturnDate(null);
        expected.get(2).setActualReturnDate(null);
        expected.remove(1);
        expected.remove(2);
        expected.remove(2);
        Mockito.when(rentalRepository.findAll()).thenReturn(expected);
        List<Rental> actual = rentalService.getByParam(userId, isActive);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByParam_isActualNull_Ok() {
        Long userId = 1L;
        Boolean isActive = null;
        List<Rental> expected = getRentals(false, 5);
        Mockito.when(rentalRepository.findRentalsByUserId(userId)).thenReturn(expected);
        List<Rental> actual = rentalService.getByParam(userId, isActive);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createRental_Ok() {
        Car car = getCar();
        Rental rental = getRental();
        rental.setCarId(car.getId());
        Mockito.when(carService.getById(rental.getCarId())).thenReturn(car);
        Mockito.when(carService.update(car)).thenReturn(car);
        rentalService.createRental(rental);
        Mockito.verify(carService, times(1)).update(car);
    }

    @Test
    void createRental_throwsException() {
        Rental rental = getRental();
        Car car = getCar();
        car.setInventory(0);
        Mockito.when(carService.getById(rental.getCarId())).thenReturn(car);
        Assertions.assertThrows(RuntimeException.class, () -> rentalService.createRental(rental));
    }

    @Test
    void closeRental_Ok() {
        Rental rental = getRental();
        rental.setActualReturnDate(null);
        Car car = getCar();
        rental.setCarId(car.getId());
        Mockito.when(carService.getById(rental.getCarId())).thenReturn(car);
        car.setInventory(car.getInventory() + 1);
        Mockito.when(carService.update(car)).thenReturn(car);
        rentalService.closeRental(rental);
        Mockito.verify(carService, times(1)).update(car);
    }

    @Test
    void closeRental_nonMoreActive_throwsException() {
        Rental rental = getRental();
        Assertions.assertThrows(RuntimeException.class, () -> rentalService.closeRental(rental));
    }

    private Rental getRental() {
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setRentalDate(LocalDateTime.parse("2023-06-20T00:00:00"));
        rental.setReturnDate(LocalDateTime.parse("2023-06-20T23:59:59"));
        rental.setActualReturnDate(LocalDateTime.parse("2023-06-21T12:00:00"));
        rental.setCarId(1L);
        rental.setUserId(1L);
        return rental;
    }

    private List<Rental> getRentals(boolean isActive, int count) {
        List<Rental> rentals = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Rental rental = getRental();
            rental.setId(i);
            if (isActive) {
                rental.setActualReturnDate(null);
            }
            rentals.add(rental);
        }
        return rentals;
    }

    private Car getCar() {
        Car car = new Car();
        car.setId(1L);
        car.setType(Car.Type.UNIVERSAL);
        car.setBrand("BMW");
        car.setModel("X8");
        car.setInventory(5);
        car.setDailyFee(BigDecimal.TEN);
        return car;
    }
}
