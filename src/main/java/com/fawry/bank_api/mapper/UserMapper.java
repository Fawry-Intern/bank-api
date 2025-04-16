package com.fawry.bank_api.mapper;

import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    public UserDetailsResponse toUserResponse(User user) {

        return new UserDetailsResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getIsActive(),
                Optional.ofNullable(user.getAccount()).map(account -> account.getId()),
                Optional.ofNullable(user.getAccount()).map(account -> account.getIsActive())
        );
    }
}
