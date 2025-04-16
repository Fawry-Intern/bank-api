package com.fawry.bank_api.repository;

import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCardNumberAndCvv(String cardNumber, String cvv);

    @Query("SELECT a FROM Account a WHERE a.user.email = :email")
    Optional<Account> findByUserEmail(@Param("email") String email);
}