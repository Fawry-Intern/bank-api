package com.fawry.bank_api.repository;

import com.fawry.bank_api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
