package com.fawry.bank_api.controller;

import com.fawry.bank_api.dto.auth.AuthenticationRequest;
import com.fawry.bank_api.dto.auth.AuthenticationResponse;
import com.fawry.bank_api.dto.auth.RegisterRequest;
import com.fawry.bank_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest logInRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(logInRequest));
    }
    @PostMapping("/sign-up")
    public ResponseEntity<Map<String,String>> singUp(@Valid @RequestBody RegisterRequest signUpRequest) {
        authenticationService.register(signUpRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
