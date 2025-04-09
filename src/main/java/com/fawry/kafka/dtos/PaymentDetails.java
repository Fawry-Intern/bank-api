package com.fawry.kafka.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PaymentDetails {
    private String number;
    private String cvv;
    private String expiry;
}
