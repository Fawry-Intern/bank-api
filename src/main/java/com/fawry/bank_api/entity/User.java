package com.fawry.bank_api.entity;

import com.fawry.bank_api.enums.UserRole;
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
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;


    @JoinColumn(name = "account_id")
    @OneToOne
    private Account account;


    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Invalid phone number format")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Column(name = "user_address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "User role is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public @NotBlank(message = "First name is required") @Size(max = 20, message = "First name must not exceed 20 characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") @Size(max = 20, message = "First name must not exceed 20 characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required") @Size(max = 20, message = "Last name must not exceed 20 characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") @Size(max = 20, message = "Last name must not exceed 20 characters") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Invalid phone number format") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9]{10,20}$", message = "Invalid phone number format") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Address is required") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address is required") String address) {
        this.address = address;
    }


    public @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }


    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        this.password = password;
    }

    public @NotNull(message = "User role is required") UserRole getRole() {
        return role;
    }

    public void setRole(@NotNull(message = "User role is required") UserRole role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User( String firstName, String lastName, String phoneNumber, String address, Boolean isActive, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isActive=isActive;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public User(){

    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
    this.isActive=isActive;
    }
}