package com.fawry.bank_api.mapper;

import com.fawry.bank_api.dto.account.AccountDetailsResponse;
import com.fawry.bank_api.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDetailsResponse toAccountResponse(Account account) {
        return new AccountDetailsResponse(
                account.getId(),
                account.getUser().getId(),
                account.getCardNumber(),
                account.getBalance(),
                account.getIsActive(),
                account.getCreatedAt()
        );
    }
}