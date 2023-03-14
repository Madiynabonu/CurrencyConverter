package com.company.service;

import com.company.entity.Customer;
import com.company.files.WorkWithFiles;
import com.company.db.Database;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.Random;

public class CustomerService {
    public static Customer getCustomerByChatId(String chatId){
        return Database.customerList.stream()
                .filter(customer -> customer.getChatId().equals(chatId))
                .findFirst().orElse(null);
    }

    public static Customer getCustomerByPhoneNumber(String phoneNumber){
        return Database.customerList.stream()
                .filter(customer -> customer.getPhoneNumber().equals(phoneNumber))
                .findFirst().orElse(null);
    }

    public static Customer addCustomer(String chatId, Contact contact){
        if(getCustomerByChatId(chatId) != null) return null;
        if(getCustomerByPhoneNumber(contact.getPhoneNumber()) != null) return null;

        String code = String.valueOf(new Random().nextInt(1000, 10000));

        Customer customer = new Customer(chatId, contact.getFirstName(),
                contact.getLastName(), contact.getPhoneNumber(), code);

        Database.customerList.add(customer);
        WorkWithFiles.writeCustomerList();
        return customer;
    }
}
