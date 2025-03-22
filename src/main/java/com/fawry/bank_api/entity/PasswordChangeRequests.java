package com.fawry.bank_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_change_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "token")
    private String token;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @Column(nullable = false,name = "expiration_date")
    private LocalDateTime expirationDate;

    public PasswordChangeRequests(String hashedToken, LocalDateTime time, User user) {
        this.token=hashedToken;
        this.expirationDate=time;
        this.user=user;
    }


    public Boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
}