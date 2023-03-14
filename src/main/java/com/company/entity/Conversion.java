package com.company.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class Conversion {
    private String chatId;
    private String firstName;
    private double amount=0;
    private String currencyId;
    private String date;
    private String currencyName;
}
