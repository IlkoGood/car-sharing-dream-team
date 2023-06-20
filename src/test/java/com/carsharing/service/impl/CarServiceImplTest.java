package com.carsharing.service.impl;

import static org.mockito.Mockito.times;

import com.carsharing.model.Car;
import com.carsharing.repository.CarRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private TelegramNotificationService telegramNotificationService;

    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Ok() {
        Car expected = getCar();
        Mockito.when(carRepository.save(expected)).thenReturn(expected);
        Car actual = carService.save(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramNotificationService, times(1))
                .generateMessageToAdministrators("Car was added to DB");
    }

    @Test
    void getById_Ok() {
        Long id = 1L;
        Car expected = getCar();
        Mockito.when(carRepository.findById(id)).thenReturn(Optional.of(expected));
        Car actual = carService.getById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getById_nonExisting_throwsException() {
        Long id = 1L;
        Mockito.when(carRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> carService.getById(id));
    }

    @Test
    void update_Ok() {
        Car expected = getCar();
        Mockito.when(carRepository.existsById(expected.getId())).thenReturn(true);
        expected.setInventory(10);
        Mockito.when(carRepository.save(expected)).thenReturn(expected);
        Car actual = carService.update(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(telegramNotificationService, times(1))
                .generateMessageToAdministrators("Car: " + expected + " was updated in DB");
    }

    @Test
    void update_nonExisting_throwsException() {
        Car car = getCar();
        Mockito.when(carRepository.existsById(car.getId())).thenReturn(false);
        Assertions.assertThrows(RuntimeException.class, () -> carService.update(car));
    }

    @Test
    void delete_Ok() {
        Long id = 1L;
        Mockito.when(carRepository.existsById(id)).thenReturn(true);
        carService.delete(id);
        Mockito.verify(carRepository, times(1)).deleteById(id);
        Mockito.verify(telegramNotificationService, times(1))
                .generateMessageToAdministrators("Car by id:" + id + " was deleted");
    }

    @Test
    void delete_nonExisting_throwsException() {
        Long id = 1L;
        Mockito.when(carRepository.existsById(id)).thenReturn(false);
        Assertions.assertThrows(RuntimeException.class, () -> carService.delete(id));
    }

    @Test
    void getAll_Ok() {
        List<Car> expected = getCars(5);
        Mockito.when(carRepository.findAll()).thenReturn(expected);
        List<Car> actual = carService.getAll();
        Assertions.assertEquals(expected, actual);
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

    private List<Car> getCars(int count) {
        List<Car> cars = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Car car = getCar();
            car.setId(i);
            cars.add(car);
        }
        return cars;
    }
}
