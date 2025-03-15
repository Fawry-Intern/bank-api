package com.fawry.bank_api.service;

import com.fawry.bank_api.dto.auth.AuthenticationRequest;
import com.fawry.bank_api.dto.auth.AuthenticationResponse;
import com.fawry.bank_api.dto.auth.RegisterRequest;

public interface AuthenticationService {
    Boolean register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
