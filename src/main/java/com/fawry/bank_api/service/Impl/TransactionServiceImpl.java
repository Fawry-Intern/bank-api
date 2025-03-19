package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.transaction.DepositRequest;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.dto.transaction.WithdrawRequest;
import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.Transaction;
import com.fawry.bank_api.enums.TransactionType;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.exception.InsufficientFundsException;
import com.fawry.bank_api.mapper.TransactionMapper;
import com.fawry.bank_api.repository.AccountRepository;
import com.fawry.bank_api.repository.TransactionRepository;
import com.fawry.bank_api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionDetailsResponse deposit(DepositRequest depositRequest) {
        Account account = accountRepository.findById(depositRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + depositRequest.accountId()));

        if (!account.getIsActive()) {
            throw new IllegalActionException("Cannot deposit to an inactive account");
        }

        BigDecimal newBalance = account.getBalance().add(depositRequest.amount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                account,
                TransactionType.DEPOSIT,
                depositRequest.note(),
                depositRequest.amount()
        );

        transaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionDetailsResponse withdraw(WithdrawRequest withdrawRequest) {
        Account account = accountRepository.findById(withdrawRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + withdrawRequest.accountId()));

        if (!account.getIsActive()) {
            throw new IllegalActionException("Cannot withdraw from an inactive account");
        }

        if (account.getBalance().compareTo(withdrawRequest.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        BigDecimal newBalance = account.getBalance().subtract(withdrawRequest.amount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                account,
                TransactionType.WITHDRAW,
                withdrawRequest.note(),
                withdrawRequest.amount()
        );

        transaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }
}