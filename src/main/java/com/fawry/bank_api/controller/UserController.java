package com.fawry.bank_api.controller;


import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.service.Impl.PasswordResetServiceImpl;
import com.fawry.bank_api.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final PasswordResetServiceImpl passwordResetService;

    public UserController(UserService userService, PasswordResetServiceImpl passwordResetService) {
        this.userService = userService;

        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Long>changeUserAccountPassword
            (@Valid @RequestBody PasswordChangeRequest passwordChangeRequest)
    {
        return ResponseEntity.ok(
                passwordResetService.changeUserAccountPassword(passwordChangeRequest));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetUserAccountPassword
            (@Valid @RequestBody PasswordResetRequest passwordResetRequest) throws NoSuchAlgorithmException {
        passwordResetService.resetPassword(passwordResetRequest);
        return
                new ResponseEntity<>("password has been set successfully", HttpStatus.OK);
    }
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> resetPasswordRequest
            (@Valid @Email @RequestParam String email) throws NoSuchAlgorithmException {
        passwordResetService.passwordResetRequest(email);
        return
                new ResponseEntity<>("check your email, a request has been sent to you ", HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> getAllUsers()
    {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/active-account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> getUsersWithActiveAccounts()
    {
        return ResponseEntity.ok(userService.getUsersWithActiveAccounts());
    }
    @GetMapping("un-active-account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> getUsersWithUnActiveAccounts()
    {
        return ResponseEntity.ok(userService.getUsersWithUnActiveAccounts());
    }

    @PutMapping("/activate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> activateUser( @PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.activateUser(userId));
    }
    @PutMapping("/deactivate/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deactivateUser
            ( @PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }


}
