package com.fawry.bank_api.mapper;

import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDetailsResponse toTransactionResponse(Transaction transaction) {
        return new TransactionDetailsResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getType(),
                transaction.getNote(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }
}