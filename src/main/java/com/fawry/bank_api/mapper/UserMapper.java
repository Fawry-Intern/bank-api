package com.fawry.bank_api.mapper;

import com.fawry.bank_api.dto.auth.RegisterRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public UserDetailsResponse toUserResponse(User user) {
        return
                new UserDetailsResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getIsActive()
        );
    }


}