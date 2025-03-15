package com.fawry.bank_api.dto.auth;

import jakarta.validation.constraints.NotNull;

public record AuthenticationResponse
        (
               String accessToken,
               Long userId
        ) {
}
