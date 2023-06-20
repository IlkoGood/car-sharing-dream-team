package com.carsharing.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import com.carsharing.bot.TelegramCarSharingBot;
import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest {
    @InjectMocks
    private TelegramNotificationService telegramNotificationService;
    @Mock
    private TelegramCarSharingBot telegramCarSharingBot;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_forClosedRentals_Ok() throws TelegramApiException {
        Rental rental = getRental();
        Mockito.when(userRepository.findById(rental.getUserId())).thenReturn(Optional.of(getUser()));
        Mockito.when(carRepository.findById(rental.getCarId())).thenReturn(Optional.of(getCar()));
        telegramNotificationService.sendNotification(rental);
        Mockito.verify(telegramCarSharingBot, times(1))
                .execute(any(SendMessage.class));
    }

    @Test
    void sendNotification_forActiveRentals_Ok() throws TelegramApiException {
        Rental rental = getActiveRentals(1).get(0);
        Mockito.when(userRepository.findById(rental.getUserId())).thenReturn(Optional.of(getUser()));
        Mockito.when(carRepository.findById(rental.getCarId())).thenReturn(Optional.of(getCar()));
        telegramNotificationService.sendNotification(rental);
        Mockito.verify(telegramCarSharingBot, times(1))
                .execute(any(SendMessage.class));
    }

    @Test
    void sendNotification_generateRentalNotificationMessage_cantFindCars_throwsExceptions() {
        Rental rental = getRental();
        Mockito.when(userRepository.findById(rental.getUserId())).thenReturn(Optional.of(getUser()));
        Mockito.when(carRepository.findById(rental.getCarId())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,
                () -> telegramNotificationService.sendNotification(rental));
    }

    @Test
    void sendNotification_userNotFound_throwsException() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,
                () -> telegramNotificationService.sendNotification(new Rental()));
    }

    @Test
    void sendNotification_catchExceptionOnTryCatch_throwsException() throws TelegramApiException {
        telegramCarSharingBot.execute(new SendMessage());
        Assertions.assertThrows(RuntimeException.class,
                () -> telegramNotificationService.sendNotification(new Rental()));
    }

    @Test
    void generateMessageToAdministrators_Ok() throws TelegramApiException {
        Mockito.when(userRepository.findAllByRole(User.Role.MANAGER)).thenReturn(getAdmins(3));
        telegramNotificationService.generateMessageToAdministrators("some message");
        Mockito.verify(telegramCarSharingBot, times(3))
                .execute(ArgumentMatchers.any(SendMessage.class));
    }

    @Test
    void sendOverdueRentalNotifications_Ok() throws TelegramApiException {
        List<Rental> overdueRentals = getActiveRentals(5);
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        Mockito.when(rentalRepository
                .findRentalsByReturnDateBeforeAndActualReturnDateIsNull(timeNow))
                .thenReturn(overdueRentals);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(getUser()));
        telegramNotificationService.sendOverdueRentalNotifications();
        Mockito.verify(telegramCarSharingBot, times(5))
                .execute(any(SendMessage.class));
    }

    @Test
    void sendOverdueRentalNotifications_userNotFound_throwsException() throws TelegramApiException {
        List<Rental> overdueRentals = getActiveRentals(5);
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        Mockito.when(rentalRepository
                        .findRentalsByReturnDateBeforeAndActualReturnDateIsNull(timeNow))
                .thenReturn(overdueRentals);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class,
                () -> telegramNotificationService.sendOverdueRentalNotifications());
    }

    @Test
    void sendOverdueRentalNotifications_ifNotPresent_Ok() throws TelegramApiException {
        List<Rental> overdueRentals = Collections.emptyList();
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        Mockito.when(rentalRepository
                        .findRentalsByReturnDateBeforeAndActualReturnDateIsNull(timeNow))
                .thenReturn(overdueRentals);
        Mockito.when(userRepository.findAll()).thenReturn(getUsers(4));
        telegramNotificationService.sendOverdueRentalNotifications();
        Mockito.verify(telegramCarSharingBot, times(4)).execute(any(SendMessage.class));
    }

    private List<User> getAdmins(int count) {
        List<User> admins = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            User admin = getUser();
            admin.setId(i);
            admin.setChatId(i);
            admin.setEmail(i + admin.getEmail());
            admins.add(admin);
        }
        return admins;
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

    private List<Rental> getRentals(int count) {
        List<Rental> rentals = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            Rental rental = getRental();
            rental.setId(i);
            rentals.add(rental);
        }
        return rentals;
    }

    private List<Rental> getActiveRentals(int count) {
        List<Rental> rentals = getRentals(count);
        for (int i = 0; i < count; i++) {
            rentals.get(i).setActualReturnDate(null);
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

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setPassword("encrypt(12345user)");
        user.setRole(User.Role.MANAGER);
        user.setChatId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        return user;
    }

    private List<User> getUsers(int count) {
        List<User> users = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            User user = getUser();
            user.setId(i);
            users.add(user);
        }
        return users;
    }
}
