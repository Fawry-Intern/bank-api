package com.fawry.bank_api.repository;

import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);

    @Query(value = """
            select * from transactions
            where transaction_id in (
            	select transaction_id from transactions
            	where order_id = :orderId
            )
            """, nativeQuery = true)
    List<Transaction> findOrderTransactionByOrderId(@Param("orderId") Long orderId);
}