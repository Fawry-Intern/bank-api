package com.fawry.bank_api.entity;

import com.fawry.bank_api.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Size(max = 1000, message = "Transaction note cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String transactionNote;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}