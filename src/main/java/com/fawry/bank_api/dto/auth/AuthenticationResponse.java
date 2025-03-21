package com.fawry.bank_api.dto.auth;

import com.fawry.bank_api.enums.UserRole;
import jakarta.validation.constraints.NotNull;

public record AuthenticationResponse
        (
               String accessToken,
               Long userId,
               UserRole role
        ) {
}
