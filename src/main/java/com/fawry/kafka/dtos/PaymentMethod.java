package com.fawry.kafka.dtos;

import lombok.Builder;

@Builder
public record PaymentMethod(
        PaymentDetails details
) {

    @Override
    public PaymentDetails details() {
        return details;
    }

    @Override
    public String toString() {
        return "" + details;
    }
}

