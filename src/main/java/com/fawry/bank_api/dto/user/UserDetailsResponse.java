package com.fawry.bank_api.dto.user;

import com.fawry.bank_api.entity.User;

public record UserDetailsResponse
        (  Long  id,
            String firstName,
           String lastName,
           String email,
           String phoneNumber,
           String address,
           Boolean isActive
        ) {

}
