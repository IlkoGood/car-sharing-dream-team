package com.carsharing.service.impl;

import com.carsharing.bot.TelegramCarSharingBot;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.carsharing.service.NotificationService;
import com.carsharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramCarSharingBot telegramCarSharingBot;
    private final UserService userService;

    @Override
    public void sendNotification(Rental rental) {
        SendMessage sendMessage = new SendMessage();
        User userById = userService.findById(rental.getUser().getId());
        sendMessage.setChatId(String.valueOf(userById.getChatId()));
        String text = "You have successfully rented a car: "
                + rental.getCar().getModel()
                + " day of rental date: " + rental.getRentalDate().toString()
                + " Your daily fee: " + rental.getCar().getDailyFee()
                + " You should return car " + rental.getReturnDate().toString();
        sendMessage.setText(text);
        try {
            telegramCarSharingBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
