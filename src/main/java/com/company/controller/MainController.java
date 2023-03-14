package com.company.controller;

import com.company.db.Database;
import com.company.entity.Conversion;
import com.company.entity.Currency;
import com.company.entity.Customer;
import com.company.enums.InlineMenuType;
import com.company.files.WorkWithFiles;
import com.company.qrcode.GenerateQRCode;
import com.company.service.CustomerService;
import com.company.util.InlineKeyboardUtil;
import com.company.util.KeyboardButtonConstants;
import com.company.util.KeyboardButtonUtil;
import com.company.container.ComponentContainer;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MainController {

    public static void handleMessage(User user, Message message) {

        if (message.hasText()) {
            String text = message.getText();
            handleText(user, message, text);
        } else if (message.hasContact()) {
            Contact contact = message.getContact();
            handleContact(user, message, contact);
        }
    }

    private static void handleContact(User user, Message message, Contact contact) {

//        if(!contact.getPhoneNumber().matches("(\\+)?998\\d{9}")) return;

        String chatId = String.valueOf(message.getChatId());
        Customer customer = CustomerService.getCustomerByChatId(chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (customer == null) {
            customer = CustomerService.addCustomer(chatId, contact);

            try {
                File qrCodeFile = GenerateQRCode.getQRCodeFile(chatId, customer.getConfirmPassword());

                SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(qrCodeFile));
                sendPhoto.setReplyMarkup(new ReplyKeyboardRemove(true));
                ComponentContainer.MY_BOT.sendMsg(sendPhoto);
                return;
            } catch (WriterException | NotFoundException | IOException e) {
                sendMessage.setText("Qayta urunib ko'ring. ");
                ComponentContainer.MY_BOT.sendMsg(sendMessage);
            }
        }else{
            sendMessage.setText("Menu: ");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getCustomerMenu());
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        }
    }

    private static void handleText(User user, Message message, String text) {
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);


        if (text.equals("/start")){
            Customer customer= CustomerService.getCustomerByChatId(chatId);
            if (customer == null) {
                sendMessage.setText("Assalomu alaykum!\nBotdan to'liq foydalanish uchun " +
                        "telefon raqamingizni jo'nating.");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
                ComponentContainer.MY_BOT.sendMsg(sendMessage);

            }else {

                sendMessage.setText("Assalomu alaykum.\n" +
                        "<b>Valyuta convertor</b> botga xush kelibsiz");
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getCustomerMenu());
                ComponentContainer.MY_BOT.sendMsg(sendMessage);
            }

        } else if (text.equals("/help")){
            String  helpText = "Yordam olish uchun ushbu " +
                    "[havola](https://www.youtube.com/watch?v=LasDZEu3VCc) ga o'ting";

            sendMessage.setText(helpText);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.disableWebPagePreview();
            ComponentContainer.MY_BOT.sendMsg(sendMessage);
        } else {

            Customer customer = CustomerService.getCustomerByChatId(chatId);
            if (!customer.isActive()){
                if (text.equals(customer.getConfirmPassword())){
                    customer.setActive(true);
                    WorkWithFiles.writeCustomerList();

                    sendMessage.setText("Menu");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.getCustomerMenu());
                    ComponentContainer.MY_BOT.sendMsg(sendMessage);
                }else {
                    sendMessage.setText("Kod noto'g'ri");
                    ComponentContainer.MY_BOT.sendMsg(sendMessage);
                }
            }else {
                // operations of user in active mode means code confirmed
                if (text.equals(KeyboardButtonConstants.SELECT_CURRENCY)){
                    sendMessage.setText("<b>Ma'lumot olish uchun valyuta tanlang: </b>");
                    sendMessage.setParseMode(ParseMode.HTML);
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.getCurrencyMenu(InlineMenuType.INFO));

                    ComponentContainer.MY_BOT.sendMsg(sendMessage);
                }else if (text.equals(KeyboardButtonConstants.CONVERSION_TO_SUM)){
                    sendMessage.setText("Qaysi valyutani so'mga o'girmoqchisiz?\n" +
                            "Tanlang: ");
                    sendMessage.setParseMode(ParseMode.HTML);
                    sendMessage.setReplyMarkup(InlineKeyboardUtil.getCurrencyMenu(InlineMenuType.CONVERSION));
                    ComponentContainer.MY_BOT.sendMsg(sendMessage);
                }else if (ComponentContainer.CAN_ENTER_AMOUNT) {
                    if (text.endsWith(".f") || text.endsWith(".d")
                            || text.endsWith("d") || text.endsWith("f")
                    || text.startsWith(".")){
                        sendMessage.setText("Xato kiritildi");
                        ComponentContainer.MY_BOT.sendMsg(sendMessage);
                    }else {
                        try {
                            double amount = Double.parseDouble(text);
                            if (amount < 0){
                                sendMessage.setText("Manfiy son kiritish mumkin emas");
                                ComponentContainer.MY_BOT.sendMsg(sendMessage);
                            }else {

                                double rate = Double.parseDouble(customer.getCurrency().getRate());
                                DecimalFormat df = new DecimalFormat("#.00");
                                String format = df.format(amount * rate);
                                sendMessage.setText("<b>" + amount +" "+customer.getCurrency().getCcyNmUZ()+" ➡️ " +
                                        ""+ format+" so'm bo'ladi.</b>");
                                sendMessage.setParseMode(ParseMode.HTML);
                                ComponentContainer.MY_BOT.sendMsg(sendMessage);

                                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                LocalDateTime localDateTime=LocalDateTime.now();
                                String date = formatter.format(localDateTime);

                                Conversion conversion = new Conversion(chatId, customer.getFirstName(),
                                        amount, String.valueOf(customer.getCurrency().getId())
                                        ,date, customer.getCurrency().getCcyNmUZ());

                                Database.conversionList.add(conversion);

                                WorkWithFiles.writeConversionList();
                                WorkWithFiles.readConversionList();
                            }
                        }catch (NumberFormatException e){
                            sendMessage.setText("Noto'g'ri amal kiritildi");
                            ComponentContainer.MY_BOT.sendMsg(sendMessage);
                        }
                    }
                }
            }
        }
    }

    public static void handleCallback(User user, Message message, String data) {
        String chatId = String.valueOf(message.getChatId());
        Integer messageId = message.getMessageId();
        SendMessage sendMessage=new SendMessage();

        sendMessage.setChatId(chatId);
        DeleteMessage deleteMessage=new DeleteMessage(chatId, messageId);

        if (data.endsWith("info")){

            ComponentContainer.MY_BOT.sendMsg(deleteMessage);
            Database.currencyList.forEach(currency -> {
                if ((currency.getId()+"info").equals(data)){
                    String s=(currency.getDiff().startsWith("-"))?" so'mga pastlagan":" so'mga ko'tarilgan";
                    sendMessage.setText("\uD83D\uDCD1 Ma'lumot:\n\n" +
                            "\uD83D\uDCB4 Valyuta: " + currency.getCcyNmUZ() + "\n" +
                            "\uD83D\uDCCA Kurs: " + currency.getRate() + " so'm\n" +
                            "\uD83D\uDCC8 So'ngi o'zgarish: " + currency.getDiff() + s + "\n" +
                            "\uD83D\uDCC5 Sana: " + currency.getDate() + " holatiga ko'ra");
                    ComponentContainer.MY_BOT.sendMsg(sendMessage);
                    return;
                }
            });
        }else if (data.endsWith("conversion")){
            Customer customer = CustomerService.getCustomerByChatId(chatId);
            ComponentContainer.MY_BOT.sendMsg(deleteMessage);
            String[] dataArray=data.split("/");
            try {
                int id=Integer.parseInt(dataArray[0]);
                Optional<Currency> currency = Database.currencyList.stream().
                        filter(currency1 -> currency1.getId() == id).
                        findFirst();
                sendMessage.setText("Qancha miqdordagi "+currency.get().getCcyNmUZ()+"ni so'mga aylantirmoqchisiz?\n" +
                        "Yetarlicha summani kiriting: ");
                customer.setCurrency(getCurrency(dataArray[0]));

                ComponentContainer.MY_BOT.sendMsg(sendMessage);

                ComponentContainer.CAN_ENTER_AMOUNT = true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
    public static Currency getCurrency(String currencyId){
        int id=Integer.parseInt(currencyId);

        for (Currency currency : Database.currencyList) {
            if (id == currency.getId()) {
                return currency;
            }
        }
        return null;
    }
}
