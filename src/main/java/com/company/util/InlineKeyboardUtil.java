package com.company.util;

import com.company.db.Database;
import com.company.enums.InlineMenuType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardUtil {

    public static ReplyKeyboard getCurrencyMenu(InlineMenuType type) {
        InlineKeyboardMarkup markup=new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> listOfLists = new ArrayList<>();
        for (int i = 0; i < 72; i+=3) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            InlineKeyboardButton button2 = new InlineKeyboardButton();
            InlineKeyboardButton button3 = new InlineKeyboardButton();

            List<InlineKeyboardButton> buttonList=new ArrayList<>();

            button.setText(Database.countryList.get(i) + " " + Database.currencyList.get(i).getCcy());

            button2.setText(Database.countryList.get(i+1) + " " + Database.currencyList.get(i+1).getCcy());
            button3.setText(Database.countryList.get(i+2) + " " + Database.currencyList.get(i+2).getCcy());
            if (type.equals(InlineMenuType.INFO)) {
                button.setCallbackData(Database.currencyList.get(i).getId() + "info");
                button2.setCallbackData(Database.currencyList.get(i + 1).getId() + "info");
                button3.setCallbackData(Database.currencyList.get(i + 2).getId() + "info");
            }else if (type.equals(InlineMenuType.CONVERSION)){
                button.setCallbackData(Database.currencyList.get(i).getId() + "/conversion");
                button2.setCallbackData(Database.currencyList.get(i + 1).getId() + "/conversion");
                button3.setCallbackData(Database.currencyList.get(i + 2).getId() + "/conversion");
            }
            buttonList.add(button);
            buttonList.add(button2);
            buttonList.add(button3);
            listOfLists.add(buttonList);
        }

        markup.setKeyboard(listOfLists);

        return markup;
    }

    public static ReplyKeyboard getConvertMenu() {
        List<List<InlineKeyboardButton>> listOfList=new ArrayList<>();

        InlineKeyboardButton button2=new InlineKeyboardButton();
        button2.setText("+1");
        button2.setCallbackData("+1");
        InlineKeyboardButton button4=new InlineKeyboardButton();
        button4.setText("+10");
        button4.setCallbackData("+10");
        InlineKeyboardButton button6=new InlineKeyboardButton();
        button6.setText("+100");
        button6.setCallbackData("+100");
        InlineKeyboardButton button8=new InlineKeyboardButton();
        button8.setText("+1000");
        button8.setCallbackData("+1000");
        List<InlineKeyboardButton> buttonList=new ArrayList<>();
        buttonList.add(button2);
        buttonList.add(button4);
        buttonList.add(button6);
        buttonList.add(button8);


        InlineKeyboardButton button1=new InlineKeyboardButton();
        button1.setText("-1");
        button1.setCallbackData("-1");
        InlineKeyboardButton button3=new InlineKeyboardButton();
        button3.setText("-10");
        button3.setCallbackData("-10");
        InlineKeyboardButton button5=new InlineKeyboardButton();
        button5.setText("-100");
        button5.setCallbackData("-100");
        InlineKeyboardButton button7=new InlineKeyboardButton();
        button7.setText("-1000");
        button7.setCallbackData("-1000");

        List<InlineKeyboardButton> buttonList1=new ArrayList<>();
        buttonList1.add(button1);
        buttonList1.add(button3);
        buttonList1.add(button5);
        buttonList1.add(button7);

        listOfList.add(buttonList);
        listOfList.add(buttonList1);
        InlineKeyboardButton convertButton=new InlineKeyboardButton();
        convertButton.setText("\uD83D\uDD04Convert");
        convertButton.setCallbackData("convert");
        List<InlineKeyboardButton> list=new ArrayList<>();
        list.add(convertButton);
        listOfList.add(list);


        InlineKeyboardMarkup markup=new InlineKeyboardMarkup();
        markup.setKeyboard(listOfList);



        return markup;
    }
}
