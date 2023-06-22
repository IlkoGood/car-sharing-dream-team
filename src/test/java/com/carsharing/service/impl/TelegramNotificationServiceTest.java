package com.carsharing.service.impl;

import com.carsharing.bot.TelegramCarSharingBot;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.repository.UserRepository;
import com.carsharing.util.UtilForTests;
import java.time.LocalDateTime;
import java.util.Collections;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest extends UtilForTests {
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

    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendNotification_forClosedRentals_Ok() throws TelegramApiException {
        Rental rental = getRental();
        Mockito.when(userRepository.findById(rental.getUserId())).thenReturn(Optional.of(getUser()));
        Mockito.when(carRepository.findById(rental.getCarId())).thenReturn(Optional.of(getCar()));
        telegramNotificationService.sendNotification(rental);
        Mockito.verify(telegramCarSharingBot, Mockito.times(1))
                .execute(Mockito.any(SendMessage.class));
    }

    @Test
    void sendNotification_forActiveRentals_Ok() throws TelegramApiException {
        Rental rental = getRentals(true, 1).get(0);
        Mockito.when(userRepository.findById(rental.getUserId())).thenReturn(Optional.of(getUser()));
        Mockito.when(carRepository.findById(rental.getCarId())).thenReturn(Optional.of(getCar()));
        telegramNotificationService.sendNotification(rental);
        Mockito.verify(telegramCarSharingBot, Mockito.times(1))
                .execute(Mockito.any(SendMessage.class));
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
        Mockito.when(userRepository.findAllByRole(User.Role.MANAGER)).thenReturn(getUsers(3));
        telegramNotificationService.generateMessageToAdministrators("some message");
        Mockito.verify(telegramCarSharingBot, Mockito.times(3))
                .execute(Mockito.any(SendMessage.class));
    }

    @Test
    void sendOverdueRentalNotifications_Ok() throws TelegramApiException {
        List<Rental> overdueRentals = getRentals(true, 5);
        LocalDateTime timeNow = LocalDateTime.now().withNano(0);
        Mockito.when(rentalRepository
                .findRentalsByReturnDateBeforeAndActualReturnDateIsNull(timeNow))
                .thenReturn(overdueRentals);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(getUser()));
        telegramNotificationService.sendOverdueRentalNotifications();
        Mockito.verify(telegramCarSharingBot, Mockito.times(5))
                .execute(Mockito.any(SendMessage.class));
    }

    @Test
    void sendOverdueRentalNotifications_userNotFound_throwsException() {
        List<Rental> overdueRentals = getRentals(true, 5);
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
        Mockito.verify(telegramCarSharingBot, Mockito.times(4))
                .execute(Mockito.any(SendMessage.class));
    }
}
