package com.fawry.bank_api.entity;

import com.fawry.bank_api.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor
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

    @NotNull(message = "Account status is required")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @NotNull(message = "CVV is required")
    @Min(value = 100, message = "CVV must be a 3 or 4 digit number")
    @Max(value = 9999, message = "CVV must be a 3 or 4 digit number")
    @Column(length = 4)
    private Integer cvv;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Instant createdAt;

}