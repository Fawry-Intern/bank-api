package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.entity.PasswordChangeRequests;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.repository.PasswordChangeRequestsRepository;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.service.PasswordResetService;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.util.PasswordValidationHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordChangeRequestsRepository passwordChangeRequestsRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${frontend.url}")
    private String frontendUrl; // Inject frontend URL (e.g., http://localhost)

    @Value("${frontend.port}")
    private int frontendPort; // Inject frontend Port (e.g., 4200)

    public PasswordResetServiceImpl(UserRepository userRepository, PasswordChangeRequestsRepository passwordChangeRequestsRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordChangeRequestsRepository = passwordChangeRequestsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean passwordResetRequest(String email) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String token = generateAndStoreToken(user);

        sendPasswordResetEmail(user, token);

        return true;
    }

    private String generateAndStoreToken(User user) throws NoSuchAlgorithmException {
        String token = UUID.randomUUID().toString();

        String hashedToken = hashToken(token);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plus(5, ChronoUnit.MINUTES);
        savePasswordChangeRequest(hashedToken, expirationTime, user);

        return token;
    }

    private String hashToken(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(token.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    private void savePasswordChangeRequest(String hashedToken, LocalDateTime expirationTime, User user) {
        passwordChangeRequestsRepository.save(new PasswordChangeRequests(hashedToken, expirationTime, user));
    }

    private void sendPasswordResetEmail(User user, String token) {
        String resetLink = String.format("%s:%d/reset-password?token=%s", frontendUrl, frontendPort, token);

        // Implement your email sending logic here (e.g., using JavaMailSender)
        System.out.println("Sending reset link to " + user.getEmail());
        System.out.println("Reset Link: " + resetLink);
    }


    public Boolean verifyResetToken(String token) throws NoSuchAlgorithmException {
        String hashedToken = hashToken(token);
        Optional<PasswordChangeRequests> request = passwordChangeRequestsRepository.findByToken(hashedToken);
        if (request.isEmpty()) {
            throw new EntityNotFoundException("Password reset request not found");
        }

        if (request.get().isExpired()) {
            throw new IllegalStateException("Token has expired");
        }
        return true;
    }

    @Transactional
    public Boolean resetPassword(PasswordResetRequest passwordResetRequest) throws NoSuchAlgorithmException {

            String hashedToken = hashToken(passwordResetRequest.token());
            Optional<PasswordChangeRequests> request = passwordChangeRequestsRepository.findByToken(hashedToken);
            if (request.isEmpty()) {
                throw new EntityNotFoundException("Password reset request not found");
            }

            if (request.get().isExpired()) {
                throw new IllegalStateException("Token has expired");
            }

            User user = request.get().getUser();

            user.setPassword(passwordEncoder.encode(passwordResetRequest.newPassword()));

            return true;

    }

    @Transactional
    @Override
    public Long changeUserAccountPassword(PasswordChangeRequest passwordChangeRequest) {

        User authenticatedUser = getAuthenticatedUser();
        User user = getUserEntity(passwordChangeRequest.userId());

        if (!isSameUser(user.getId(), authenticatedUser.getId())) {
            throw new IllegalActionException("You can't change another user's account password");
        }

        if (!passwordEncoder.matches(passwordChangeRequest.oldPassword(), user.getPassword())) {
            throw new ValidationException("Old password isn't correct");
        }

        String encodedNewPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
        user.setPassword(encodedNewPassword);

        return user.getId();
    }
    private User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id= " + userId + " doesn't exist"));
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Boolean isSameUser(Long userId, Long authenticatedUserId) {
        return userId.equals(authenticatedUserId);
    }
}
