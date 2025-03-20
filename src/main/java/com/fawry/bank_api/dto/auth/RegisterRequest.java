package com.fawry.bank_api.dto.auth;

import com.fawry.bank_api.annotations.ValidPassword;
import com.fawry.bank_api.enums.UserRole;
import jakarta.validation.constraints.*;

public record RegisterRequest
        (
                @NotEmpty(message = "The first name is required.")
                @Size(min = 2, max = 30, message = "The length of first name must be between 2 and 30 characters.")
                String firstName,

                @NotEmpty(message = "The last name is required.")
                @Size(min = 2, max = 30, message = "The length of last name must be between 2 and 30 characters.")
                String lastName,

                @NotEmpty(message = "The email address is required.")
                @Email(message = "The email address is invalid.")
                String email,
                @NotBlank(message = "password mustn't be blank")
                @ValidPassword
                String password,

                @NotEmpty(message = "The phoneNumber is required.")
                @Size(min = 11, max = 15, message = "The length of phoneNumber must be between 11 and 15 Number.")
                @Pattern(regexp = "^[0-9]+$", message = "You Can Only Write Numbers")
                String phoneNumber,

                @NotEmpty(message = "The address is required.")
                String address
        ) {
}
