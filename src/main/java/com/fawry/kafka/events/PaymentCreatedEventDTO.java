package com.fawry.kafka.events;

import com.fawry.kafka.dtos.AddressDetails;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PaymentCreatedEventDTO implements Serializable {
    private Long orderId;
    private Long userId;
    private String customerEmail;
    private String customerName;
    private String customerContact;
    private AddressDetails addressDetails;
    private BigDecimal paymentAmount;
}
