package com.fawry.bank_api.mapper;

import com.fawry.bank_api.dto.auth.AuthenticationRequest;
import com.fawry.bank_api.dto.auth.AuthenticationResponse;
import com.fawry.bank_api.dto.auth.RegisterRequest;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {
    private final PasswordEncoder passwordEncoder;

    public AuthenticationMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse toAuthResponse(String token, Long userId, UserRole role)
    {
        return new AuthenticationResponse(token,userId,role);
    }
    public User toUserEntity(RegisterRequest request)
    {
        return new User(
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                request.address(),
                true,
                request.email(),
               passwordEncoder.encode( request.password()),
                request.role()
        );
    }
}
