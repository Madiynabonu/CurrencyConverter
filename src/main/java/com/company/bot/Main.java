package com.company.bot;

import com.company.container.ComponentContainer;
import com.company.files.WorkWithFiles;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main{
    public static String ADMIN_CHAT_ID = " ";
    public static void main(String[] args) {
        ComponentContainer.ADMIN_CHAT_IDS.add(ADMIN_CHAT_ID);
        try {

            WorkWithFiles.readCustomerList();
            WorkWithFiles.readConversionList();

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            CurrencyBot myBot = new CurrencyBot();
            ComponentContainer.MY_BOT = myBot;
            botsApi.registerBot(myBot);

            SendMessage sendMessage=new SendMessage();
            for (String id : ComponentContainer.ADMIN_CHAT_IDS) {
                sendMessage.setChatId(id);
                sendMessage.setText("Bot ishga tushdi.");
                ComponentContainer.MY_BOT.sendMsg(sendMessage);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}