package com.fawry.bank_api.entity;

import com.fawry.bank_api.dto.transaction.TransactionState;
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

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Account is required")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @NotNull(message = "Transaction type is required")
    @Enumerated(STRING)
    @Column(name = "transaction_type",nullable = false)
    private TransactionType type;

    @Enumerated(STRING)
    @Column(name = "state", nullable = false)
    private TransactionState state;

    @Size(max = 1000, message = "Transaction note cannot exceed 1000 characters")
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    @Column(name = "transaction_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;
}