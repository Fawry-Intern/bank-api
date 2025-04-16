package com.fawry.kafka.events;

import com.fawry.kafka.dtos.AddressDetails;
import com.fawry.kafka.dtos.PaymentMethod;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StoreCreatedEventDTO implements Serializable {
    private Long orderId;
    private Long userId;
    private String status;
    private String customerEmail;
    private String customerName;
    private String customerContact;
    private AddressDetails addressDetails;
    private BigDecimal paymentAmount;
    private PaymentMethod paymentMethod;
    private String merchantEmail;
}
