package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.account.AccountCreationRequest;
import com.fawry.bank_api.dto.account.AccountDetailsResponse;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.mapper.AccountMapper;
import com.fawry.bank_api.mapper.TransactionMapper;
import com.fawry.bank_api.repository.AccountRepository;
import com.fawry.bank_api.repository.TransactionRepository;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final SecureRandom secureRandom;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            AccountMapper accountMapper,
            TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
        this.secureRandom = new SecureRandom();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountDetailsResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDetailsResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public List<TransactionDetailsResponse> getAccountTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        return transactionRepository.findByAccountOrderByCreatedAtDesc(account).stream()
                .map(transactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountDetailsResponse createAccount(AccountCreationRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.userId()));

        if (user.getAccount() != null) {
            throw new IllegalArgumentException("User already has an account");
        }

        String cardNumber = request.cardNumber();
        if (cardNumber == null || cardNumber.isEmpty()) {
            cardNumber = generateCardNumber();
        }

        String cvv = generateCvv();

        Account account = new Account();
        account.setUser(user);
        account.setCardNumber(cardNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setIsActive(true);
        account.setCvv(cvv);

        account = accountRepository.save(account);

        user.setAccount(account);
        userRepository.save(user);

        return accountMapper.toAccountResponse(account);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AccountDetailsResponse activateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        account.setIsActive(true);
        account = accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AccountDetailsResponse deactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        account.setIsActive(false);
        account = accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    private String generateCardNumber() {
        StringBuilder builder = new StringBuilder("4"); // Starting with 4 for Visa-like numbers
        for (int i = 1; i < 16; i++) {
            builder.append(secureRandom.nextInt(10));
        }
        return builder.toString();
    }

    private String generateCvv() {
        int cvvValue = 100 + secureRandom.nextInt(900); // 3-digit number between 100-999
        return String.valueOf(cvvValue);
    }
}