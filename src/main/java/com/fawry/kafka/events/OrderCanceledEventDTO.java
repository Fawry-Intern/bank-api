package com.fawry.kafka.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@RequiredArgsConstructor
@Getter
@ToString
public class OrderCanceledEventDTO implements Serializable {

    private final Long orderId;
    private final String reason;
    private final String customerEmail;



    public static OrderCanceledEventDTO newInstance(Long orderId, String reason, String customerEmail) {
        return new OrderCanceledEventDTO(orderId, reason, customerEmail);
    }

}

