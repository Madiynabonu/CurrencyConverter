package com.company.db;

import com.company.bot.CurrencyBot;
import com.company.entity.Conversion;
import com.company.entity.Currency;
import com.company.entity.Customer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public interface Database {
    List<Customer> customerList = new ArrayList<>();
    List<Conversion> conversionList = new ArrayList<>();

    ArrayList<Currency> currencyList = createCurrencyList();

    private static ArrayList<Currency> createCurrencyList() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Currency> currencies;
        try {
            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json");
            URLConnection urlConnection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Type type = new TypeToken<ArrayList<Currency>>(){}.getType();

            currencies = gson.fromJson(reader, type);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }

    List<String> countryList = new ArrayList<>(List.of(
            "\uD83C\uDDFA\uD83C\uDDF8", "\uD83C\uDDEA\uD83C\uDDFA", "\uD83C\uDDF7\uD83C\uDDFA", "\uD83C\uDDEC\uD83C\uDDE7",
            "\uD83C\uDDEF\uD83C\uDDF5", "\uD83C\uDDE6\uD83C\uDDFF", "\uD83C\uDDE7\uD83C\uDDE9", "\uD83C\uDDE7\uD83C\uDDEC",
            "\uD83C\uDDE7\uD83C\uDDED", "\uD83C\uDDE7\uD83C\uDDF3", "\uD83C\uDDE7\uD83C\uDDF7", "\uD83C\uDDE7\uD83C\uDDFE",
            "\uD83C\uDDE8\uD83C\uDDE6", "\uD83C\uDDE8\uD83C\uDDED", "\uD83C\uDDE8\uD83C\uDDF3", "\uD83C\uDDE8\uD83C\uDDFA",
            "\uD83C\uDDE8\uD83C\uDDFF", "\uD83C\uDDE9\uD83C\uDDF0", "\uD83C\uDDE9\uD83C\uDDFF", "\uD83C\uDDEA\uD83C\uDDEC",
            "\uD83C\uDDE6\uD83C\uDDEB", "\uD83C\uDDE6\uD83C\uDDF7", "\uD83C\uDDEC\uD83C\uDDEA", "\uD83C\uDDED\uD83C\uDDF0",
            "\uD83C\uDDED\uD83C\uDDFA", "\uD83C\uDDEE\uD83C\uDDE9", "\uD83C\uDDEE\uD83C\uDDF1", "\uD83C\uDDEE\uD83C\uDDF3",
            "\uD83C\uDDEE\uD83C\uDDF6", "\uD83C\uDDEE\uD83C\uDDF7", "\uD83C\uDDEE\uD83C\uDDF8", "\uD83C\uDDEF\uD83C\uDDF4",
            "\uD83C\uDDE6\uD83C\uDDFA", "\uD83C\uDDF0\uD83C\uDDEC", "\uD83C\uDDF0\uD83C\uDDED", "\uD83C\uDDF0\uD83C\uDDF7",
            "\uD83C\uDDF0\uD83C\uDDFC", "\uD83C\uDDF0\uD83C\uDDFF", "\uD83C\uDDF1\uD83C\uDDE6", "\uD83C\uDDF1\uD83C\uDDE7",
            "\uD83C\uDDF1\uD83C\uDDFE", "\uD83C\uDDF2\uD83C\uDDE6", "\uD83C\uDDF2\uD83C\uDDE9", "\uD83C\uDDF2\uD83C\uDDF2",
            "\uD83C\uDDF2\uD83C\uDDF3", "\uD83C\uDDF2\uD83C\uDDFD", "\uD83C\uDDF2\uD83C\uDDFE", "\uD83C\uDDF3\uD83C\uDDF4",
            "\uD83C\uDDF3\uD83C\uDDFF", "\uD83C\uDDF4\uD83C\uDDF2", "\uD83C\uDDF5\uD83C\uDDED", "\uD83C\uDDF5\uD83C\uDDF0",
            "\uD83C\uDDF5\uD83C\uDDF1", "\uD83C\uDDF6\uD83C\uDDE6", "\uD83C\uDDF7\uD83C\uDDF4", "\uD83C\uDDF7\uD83C\uDDF8",
            "\uD83C\uDDE6\uD83C\uDDF2", "\uD83C\uDDF8\uD83C\uDDE6", "\uD83C\uDDF8\uD83C\uDDE9", "\uD83C\uDDF8\uD83C\uDDEA",
            "\uD83C\uDDF8\uD83C\uDDEC", "\uD83C\uDDF8\uD83C\uDDFE", "\uD83C\uDDF9\uD83C\uDDED", "\uD83C\uDDF9\uD83C\uDDEF",
            "\uD83C\uDDF9\uD83C\uDDF2", "\uD83C\uDDF9\uD83C\uDDF3", "\uD83C\uDDF9\uD83C\uDDF7", "\uD83C\uDDFA\uD83C\uDDE6",
            "\uD83C\uDDE6\uD83C\uDDEA", "\uD83C\uDDFA\uD83C\uDDFE", "\uD83C\uDDFB\uD83C\uDDEA", "\uD83C\uDDFB\uD83C\uDDF3"
    ));
}
