package com.fawry;

import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.enums.UserRole;
import com.fawry.bank_api.repository.AccountRepository;
import com.fawry.bank_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@SpringBootApplication
public class BankApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApiApplication.class, args);
    }


    @Component
    @RequiredArgsConstructor
    public class DataInitializer implements CommandLineRunner {

        private final UserRepository userRepository;
        private final AccountRepository accountRepository;
        private final BCryptPasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
            if (userRepository.count() == 0) {
                // Create User
                User user = User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("+201234567890")
                        .address("123 Main St, Cairo, Egypt")
                        .isActive(true)
                        .email("john.doe@example.com")
                        .password(passwordEncoder.encode("SecurePass123!")) // Hashed password
                        .role(UserRole.USER)
                        .createdAt(Instant.now())
                        .build();

                // Save User First
                user = userRepository.save(user);

                // Create Account linked to User
                Account account = Account.builder()
                        .user(user)  // Set user relationship
                        .cardNumber("1234567812345678")
                        .balance(BigDecimal.valueOf(5000.00))
                        .isActive(true)
                        .cvv("123")
                        .createdAt(Instant.now())
                        .build();

                // Save Account
                accountRepository.save(account);

                // Link Account to User and Save Again
                user.setAccount(account);
                userRepository.save(user);

                System.out.println("User and Account Initialized Successfully.");
            }
        }
    }

}
