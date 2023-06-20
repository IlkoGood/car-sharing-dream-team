package com.carsharing.bot;

import com.carsharing.exception.DataProcessingException;
import com.carsharing.model.User;
import com.carsharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramCarSharingBot extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String botUserName;
    @Value("${bot.token}")
    private String botToken;
    private final UserService userService;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sendStartMessage(chatId, update.getMessage().getChat().getFirstName());
                return;
            }
            User user = null;
            try {
                user = userService.findByEmail(messageText);
            } catch (DataProcessingException e) {
                sendMessage(chatId, "User with this email doesn't exist, try again");
            }
            if (user != null) {
                user.setChatId(chatId);
                userService.save(user);
                sendMessage(chatId, "User registration successful! Welcome");
            }
        }
    }

    private void sendStartMessage(Long chatId, String name) {
        String answer = "Hi, " + name + "," + " Please enter your email";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't send message", e);
        }
    }
}
