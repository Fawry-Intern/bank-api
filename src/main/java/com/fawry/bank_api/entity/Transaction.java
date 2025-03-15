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
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Account is required")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Size(max = 1000, message = "Transaction note cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String note;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public @NotNull(message = "Account is required") Account getAccount() {
        return account;
    }

    public void setAccount(@NotNull(message = "Account is required") Account account) {
        this.account = account;
    }

    public @NotNull(message = "Transaction type is required") TransactionType getType() {
        return type;
    }

    public void setType(@NotNull(message = "Transaction type is required") TransactionType type) {
        this.type = type;
    }

    public @Size(max = 1000, message = "Transaction note cannot exceed 1000 characters") String getNote() {
        return note;
    }

    public void setNote(@Size(max = 1000, message = "Transaction note cannot exceed 1000 characters") String note) {
        this.note = note;
    }

    public @NotNull(message = "Transaction amount is required") @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0") @Digits(integer = 13, fraction = 2, message = "Invalid amount format") BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotNull(message = "Transaction amount is required") @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0") @Digits(integer = 13, fraction = 2, message = "Invalid amount format") BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }



    public Transaction( Account account, TransactionType type, String note, BigDecimal amount) {

        this.account = account;
        this.type = type;
        this.note = note;
        this.amount = amount;

    }
    public Transaction()
    {

    }
}