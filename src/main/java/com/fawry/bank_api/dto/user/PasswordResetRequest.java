package com.fawry.bank_api.dto.user;

import jakarta.validation.constraints.NotNull;

public record PasswordResetRequest
        (
                @NotNull Long userId,
                @NotNull  String newPassword,
                @NotNull String confirmedPassword
        )
{
}
