package com.fawry.bank_api.dto.transaction;

import com.fawry.bank_api.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionDetailsResponse(
        Long id,
        Long accountId,
        TransactionType type,
        String note,
        BigDecimal amount,
        Instant createdAt
) {
}