package com.company.controller;

import com.company.db.Database;
import com.company.entity.Conversion;
import com.company.entity.Currency;
import com.company.entity.Customer;
import com.company.files.WorkWithFiles;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardButtonConstants;
import com.company.util.KeyboardButtonUtil;
import com.company.container.ComponentContainer;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class AdminController {
    public static void handleMessage(User user, Message message) {

        if (message.hasText()) {
            String text = message.getText();
            handleText(user, message, text);
        }
    }

    private static void handleText(User user, Message message, String text) {
        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (text.equals("/start")) {
            sendMessage.setText("Hello Admin");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(KeyboardButtonConstants.USERS_LIST)) {

            if (!Database.customerList.isEmpty()){
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(new InputFile(WorkWithFiles.generateUsersExcelFile()));
                ComponentContainer.MY_BOT.sendMsg(sendDocument);
            }else {
                sendMessage.setText("Foydalanuvchilar yo'q");
                ComponentContainer.MY_BOT.sendMsg(sendMessage);
            }

        }else if (text.equals(KeyboardButtonConstants.ALL_CURRENCY_CONVERSIONS_LIST)){
            if (Database.conversionList.isEmpty()){
                sendMessage.setText("Hozircha hech qanday amaliyot bajarilmagan");
                ComponentContainer.MY_BOT.sendMsg(sendMessage);
            }else {
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(new InputFile(WorkWithFiles.generateConversionsExcelFile()));
                ComponentContainer.MY_BOT.sendMsg(sendDocument);
            }

        }else if (text.equals(KeyboardButtonConstants.EXCHANGE_RATES_TO_SUM)){
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            sendDocument.setDocument(new InputFile(WorkWithFiles.generateExchangeRatePdfFile()));
            ComponentContainer.MY_BOT.sendMsg(sendDocument);
        } else if (text.equals(KeyboardButtonConstants.SEND_MESSAGE_TO_ALL_USERS)) {
            ComponentContainer.CAN_RECEIVE_MESSAGE = true;
            sendMessage.setText("Xabarni kiriting: ");
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        }else if (ComponentContainer.CAN_RECEIVE_MESSAGE){
            sendMessageToCustomers(text);
            ComponentContainer.CAN_RECEIVE_MESSAGE = false;
        }else {
            sendMessage.setText("Noto'g'ri amal tanlandi");
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        }
    }

    private static void sendMessageToCustomers(String message) {
        for (Customer customer : Database.customerList) {
            SendMessage sendMessage = new SendMessage(customer.getChatId(), message);
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        }
    }

    public static void handleCallback(User user, Message message, String data) {
        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        }
    }

//
//@AllArgsConstructor
//class MyThread extends Thread{
////    private Customer customer;
//    private EditMessageText editMessageText;
//    private String chatId;
//    private Conversion conversion;
//    private String amount;
//    private Integer messageId;
//
//    @Override
//    public void run() {
//        int sum = Integer.parseInt(amount);
//        EditMessageText editMessageText=new EditMessageText();
//        if (amount.startsWith("-")){
//            conversion.setAmount(conversion.getAmount()-sum);
//        }else {
//            conversion.setAmount(conversion.getAmount()+sum);
//        }
//        editMessageText.setChatId(chatId);
//        editMessageText.setMessageId(messageId);
//        editMessageText.setText("Hozirgi miqdor: "+conversion.getAmount());
//        editMessageText.setReplyMarkup((InlineKeyboardMarkup) InlineKeyboardUtil.getConvertMenu());
//        ComponentContainer.MY_BOT.sendMsg(editMessageText);
//    }
//}
