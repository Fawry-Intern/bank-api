package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.auth.AuthenticationRequest;
import com.fawry.bank_api.dto.auth.AuthenticationResponse;
import com.fawry.bank_api.dto.auth.RegisterRequest;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.enums.ResourceType;
import com.fawry.bank_api.exception.DuplicateResourceException;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.mapper.AuthenticationMapper;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.security.JwtService;
import com.fawry.bank_api.service.AuthenticationService;
import com.fawry.bank_api.util.PasswordValidationHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticationMapper authenticationMapper;

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, AuthenticationMapper authenticationMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authenticationMapper = authenticationMapper;
    }

    @Override
    public Boolean register(RegisterRequest request) {
        if (!PasswordValidationHelper.isValid(request.password())) {
            throw new IllegalActionException("Password does not meet security requirements");
        }
        User user = authenticationMapper.toUserEntity(request);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("This email already exists", ResourceType.EMAIL);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (!PasswordValidationHelper.isValid(request.password())) {
            throw new IllegalActionException("Password does not meet security requirements");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String token = jwtService.generateToken(user);

        return authenticationMapper.toAuthResponse(token, user.getId(),user.getRole());
    }
}
