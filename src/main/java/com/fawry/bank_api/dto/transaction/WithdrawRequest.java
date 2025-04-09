package com.fawry.bank_api.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WithdrawRequest(
        @NotNull Long accountId,
        @NotNull
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
        BigDecimal amount,
        @Size(max = 1000, message = "Note cannot exceed 1000 characters")
        String note
) {
}