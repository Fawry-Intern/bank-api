package com.fawry.bank_api.dto.account;

import jakarta.validation.constraints.NotNull;

public record AccountCreationRequest(
        @NotNull Long userId,
        String cardNumber
) {
}