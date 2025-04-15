package com.fawry.kafka.events;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderCanceledEventDTO implements Serializable {

    private Long orderId;
    private String reason;
    private String customerEmail;

}

