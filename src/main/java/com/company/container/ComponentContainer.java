package com.company.container;

import com.company.bot.CurrencyBot;

import java.util.ArrayList;
import java.util.List;

public class ComponentContainer {
    public static CurrencyBot MY_BOT = null;
    public static String BOT_USERNAME = "";
    public static String BOT_TOKEN = "";
    public static List<String> ADMIN_CHAT_IDS = new ArrayList<>();

    public static boolean CAN_RECEIVE_MESSAGE = false;
    public static boolean CAN_ENTER_AMOUNT = false;

}
