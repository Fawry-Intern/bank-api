package com.fawry.bank_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID is required")
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;

    @Pattern(regexp = "^[0-9]{16,20}$", message = "Card number must be between 16 and 20 digits")
    @Column(length = 20)
    private String cardNumber;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be zero or positive")
    @Column(precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Long getId() {
        return id;
    }


    public @NotNull(message = "User ID is required") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "User ID is required") User user) {
        this.user = user;
    }

    public @Pattern(regexp = "^[0-9]{16,20}$", message = "Card number must be between 16 and 20 digits") String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@Pattern(regexp = "^[0-9]{16,20}$", message = "Card number must be between 16 and 20 digits") String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public @NotNull(message = "Balance cannot be null") @PositiveOrZero(message = "Balance must be zero or positive") BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(@NotNull(message = "Balance cannot be null") @PositiveOrZero(message = "Balance must be zero or positive") BigDecimal balance) {
        this.balance = balance;
    }



    public @NotNull(message = "CVV is required") @Min(value = 100, message = "CVV must be a 3 or 4 digit number") @Max(value = 9999, message = "CVV must be a 3 or 4 digit number") String getCvv() {
        return cvv;
    }

    public void setCvv(@NotNull(message = "CVV is required") @Min(value = 100, message = "CVV must be a 3 or 4 digit number") @Max(value = 9999, message = "CVV must be a 3 or 4 digit number") String cvv) {
        this.cvv = cvv;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Account( User user, String cardNumber, BigDecimal balance, Boolean isActive, String cvv, Instant createdAt) {

        this.user = user;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.isActive=isActive;
        this.cvv = cvv;
        this.createdAt = createdAt;
    }

    public Account()
    {

    }
}