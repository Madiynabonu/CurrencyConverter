package com.company.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardButtonUtil {

    public static ReplyKeyboard getAdminMenu() {
        List<KeyboardRow> rowList = getRowList(
                getRow(getButton(KeyboardButtonConstants.USERS_LIST)),
                getRow(
                        getButton(KeyboardButtonConstants.ALL_CURRENCY_CONVERSIONS_LIST),
                        getButton(KeyboardButtonConstants.EXCHANGE_RATES_TO_SUM)
                ),
                getRow(getButton(KeyboardButtonConstants.SEND_MESSAGE_TO_ALL_USERS)
                ));

        return getMarkup(rowList);
    }

    private static KeyboardButton getButton(String demo) {
        return new KeyboardButton(demo);
    }

    private static KeyboardRow getRow(KeyboardButton... buttons) {
        return new KeyboardRow(List.of(buttons));
    }

    private static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return List.of(rows);
    }

    private static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> rowList) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rowList);
        markup.setResizeKeyboard(true);
        markup.setSelective(true);
        return markup;
    }

    public static ReplyKeyboard getContactMenu() {
        KeyboardButton button = getButton("Raqamingizni jo'nating.");
        button.setRequestContact(true);

        return getMarkup(getRowList(getRow(button)));
    }

    public static ReplyKeyboard getCustomerMenu() {

        return getMarkup(getRowList(getRow(
                getButton(KeyboardButtonConstants.SELECT_CURRENCY),
                        getButton(KeyboardButtonConstants.CONVERSION_TO_SUM))));
    }
}
