package com.company.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    private String chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String confirmPassword;
    private boolean isActive;
    private Currency currency;

    public Customer(String chatId, String firstName, String lastName, String phoneNumber, String confirmPassword) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.confirmPassword = confirmPassword;
    }
}
