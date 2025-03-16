package com.fawry.bank_api.service;

import com.fawry.bank_api.dto.transaction.DepositRequest;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.dto.transaction.WithdrawRequest;

public interface TransactionService {
    TransactionDetailsResponse deposit(DepositRequest depositRequest);
    TransactionDetailsResponse withdraw(WithdrawRequest withdrawRequest);
}