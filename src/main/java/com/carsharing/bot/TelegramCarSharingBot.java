package com.carsharing.bot;

import com.carsharing.model.User;
import com.carsharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramCarSharingBot extends TelegramLongPollingBot  {
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
                Optional<User> user = userService.findByEmail(messageText);
                if (user.isPresent()) {
                    User foundUser = user.get();
                    foundUser.setChatId(chatId);
                    userService.save(foundUser);
                    sendMessage(chatId, "Success");
                } else {
                    String answer = "User with this email doesn't exist, try again";
                    sendMessage(chatId, answer);
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
            throw new RuntimeException("Can't send message ");
        }
    }
}
