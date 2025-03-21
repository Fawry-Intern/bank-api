package com.fawry.bank_api.dto.user;

import com.fawry.bank_api.annotations.ValidPassword;
import jakarta.validation.constraints.NotNull;

public record PasswordResetRequest
        (
                @NotNull Long userId,
                @NotNull
                @ValidPassword
                String newPassword,
                @NotNull
                @ValidPassword
                String confirmedPassword
        )
{
}
