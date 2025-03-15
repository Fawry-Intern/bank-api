package com.fawry.bank_api.controller;

import com.fawry.bank_api.dto.account.AccountCreationRequest;
import com.fawry.bank_api.dto.account.AccountDetailsResponse;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountDetailsResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDetailsResponse> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDetailsResponse>> getAccountTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountTransactions(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountDetailsResponse> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @PutMapping("/activate/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDetailsResponse> activateAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.activateAccount(accountId));
    }

    @PutMapping("/deactivate/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDetailsResponse> deactivateAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.deactivateAccount(accountId));
    }
}