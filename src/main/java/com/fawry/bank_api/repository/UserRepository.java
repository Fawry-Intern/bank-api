package com.fawry.bank_api.repository;

import com.fawry.bank_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
   boolean existsByEmail(String username);
   Optional<User>findByEmail(String email);
}
