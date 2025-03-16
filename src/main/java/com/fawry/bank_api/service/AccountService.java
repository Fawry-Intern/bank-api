package com.fawry.bank_api.service;

import com.fawry.bank_api.dto.account.AccountCreationRequest;
import com.fawry.bank_api.dto.account.AccountDetailsResponse;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;

import java.util.List;

public interface AccountService {
    List<AccountDetailsResponse> getAllAccounts();
    AccountDetailsResponse getAccountById(Long accountId);
    List<TransactionDetailsResponse> getAccountTransactions(Long accountId);
    AccountDetailsResponse createAccount(AccountCreationRequest request);
    AccountDetailsResponse activateAccount(Long accountId);
    AccountDetailsResponse deactivateAccount(Long accountId);
}
