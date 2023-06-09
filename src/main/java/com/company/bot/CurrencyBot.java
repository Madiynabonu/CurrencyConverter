package com.company.bot;

import com.company.container.ComponentContainer;
import com.company.controller.AdminController;
import com.company.controller.MainController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CurrencyBot extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return ComponentContainer.BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return ComponentContainer.BOT_USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();

            String chatId = String.valueOf(message.getChatId());

            if (ComponentContainer.ADMIN_CHAT_IDS.contains(chatId)) {
                AdminController.handleMessage(user, message);
            } else {
                MainController.handleMessage(user, message);
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            User user = callbackQuery.getFrom();
            String data = callbackQuery.getData();

            String chatId = String.valueOf(message.getChatId());

            if (ComponentContainer.ADMIN_CHAT_IDS.contains(chatId)) {
                AdminController.handleCallback(user, message, data);
            } else {
                MainController.handleCallback(user, message, data);
            }
        }
    }

    public void sendMsg(Object obj) {
        try {
            if (obj instanceof SendMessage) {
                execute((SendMessage) obj);
            } else if (obj instanceof DeleteMessage) {
                execute((DeleteMessage) obj);
            } else if (obj instanceof EditMessageText) {
                execute((EditMessageText) obj);
            } else if (obj instanceof SendPhoto) {
                execute((SendPhoto) obj);
            }else if (obj instanceof SendDocument) {
                execute((SendDocument) obj);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
