package com.fawry.bank_api.controller;


import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Long>changeUserAccountPassword
            (@Valid @RequestBody PasswordChangeRequest passwordChangeRequest)
    {
        return ResponseEntity.ok(
                userService.changeUserAccountPassword(passwordChangeRequest));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<Long> resetUserAccountPassword
            (@Valid @RequestBody PasswordResetRequest passwordResetRequest)
    {
        return ResponseEntity.ok(
                userService.resetUserAccountPassword(passwordResetRequest));
    }


    @GetMapping
    public ResponseEntity<List<UserDetailsResponse>> getAllUsers()
    {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("/activate/{userId}")
    public ResponseEntity<UserDetailsResponse> activateUser( @PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.activateUser(userId));
    }
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<UserDetailsResponse> deactivateUser
            ( @PathVariable Long userId)
    {
        return ResponseEntity.ok(userService.deactivateUser(userId));
    }


}
