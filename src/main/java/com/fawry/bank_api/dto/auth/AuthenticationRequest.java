package com.fawry.bank_api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest
        (
                @NotBlank(message = "email mustn't be blank")
                String email,
                @NotBlank(message = "password mustn't be blank")
                String password
        )
{
}
