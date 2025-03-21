package com.fawry.bank_api.dto.account;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountDetailsResponse(
        Long id,
        Long userId,
        String cardNumber,
        BigDecimal balance,
        Boolean isActive,
        Instant createdAt,
        String cvv) {
}