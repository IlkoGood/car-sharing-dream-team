package com.carsharing.service.impl;

import com.carsharing.bot.TelegramCarSharingBot;
import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.repository.CarRepository;
import com.carsharing.repository.RentalRepository;
import com.carsharing.service.NotificationService;
import com.carsharing.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramCarSharingBot telegramCarSharingBot;
    private final UserService userService;
    private final CarRepository carRepository;
    private final RentalRepository rentalServiceRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void sendNotification(Rental rental) {
        SendMessage sendMessage = new SendMessage();
        User userById = userService.findById(rental.getUser().getId());
        sendMessage.setChatId(String.valueOf(userById.getChatId()));
        sendMessage.setText(generateRentalNotificationMessage(rental));
        try {
            telegramCarSharingBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't send message", e);
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void sendOverdueRentalNotifications() {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Rental> overdueRentals = rentalServiceRepository.findOverdueRentals(localDateTime);
        for (Rental rental : overdueRentals) {
            SendMessage sendMessage = new SendMessage();
            User userById = userService.findById(rental.getUser().getId());
            sendMessage.setChatId(String.valueOf(userById.getChatId()));
            sendMessage.setText(generateOverdueRentalNotification(rental, localDateTime));
            try {
                telegramCarSharingBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Can't send message", e);
            }
        }
    }

    private String generateRentalNotificationMessage(Rental rental) {
        Car car = carRepository.findById(rental.getCar().getId()).orElseThrow(() ->
                new RuntimeException("Car not found"));
        String text;
        if (rental.getActualReturnDate() == null) {
            text = "You have successfully rented a car: " + car.getModel()
                    + "\n" + "Rental date: " + rental.getRentalDate().format(formatter)
                    + "\n" + "Your daily fee: " + car.getDailyFee()
                    + "\n" + "You should return car " + rental.getReturnDate().format(formatter);
        } else {
            text = "You have successfully returned the car: " + car.getModel()
                    + "\n" + "Return date: " + rental.getActualReturnDate().format(formatter);
        }
        return text;
    }

    private String generateOverdueRentalNotification(Rental rental, LocalDateTime date) {
        String text = "It looks like you've missed a rent payment on "
                + date.format(formatter)
                + ". Please make a payment at your earliest convenience to avoid any penalties. "
                + "Thank you!";
        return text;
    }
}
