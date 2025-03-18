package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.enums.UserRole;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.mapper.UserMapper;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.service.UserService;
import com.fawry.bank_api.util.PasswordValidationHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDetailsResponse> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserDetailsResponse getUserProfile(Long userId) {
        User authenticatedUser = getAuthenticatedUser();
        User user = getUserEntity(userId);

        if (!authenticatedUser.getRole().equals(UserRole.ADMIN) && !isSameUser(user.getId(), authenticatedUser.getId())) {
            throw new IllegalActionException("Only admin can see other profiles");
        }

        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public UserDetailsResponse activateUser(Long userId) {
        User user = getUserEntity(userId);
        User authenticatedUser = getAuthenticatedUser();

        if (isSameUser(userId, authenticatedUser.getId())) {
            throw new IllegalActionException("You can't activate your account");
        }

        user.setIsActive(true);
        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public UserDetailsResponse deactivateUser(Long userId) {
        User authenticatedUser = getAuthenticatedUser();
        User user = getUserEntity(userId);

        if (isSameUser(userId, authenticatedUser.getId())) {
            throw new IllegalActionException("You can't deactivate your account");
        }

        user.setIsActive(false);
        return userMapper.toUserResponse(user);
    }

    @Transactional
    @Override
    public Long resetUserAccountPassword(PasswordResetRequest passwordResetRequest) {
        if (!PasswordValidationHelper.isValid(passwordResetRequest.newPassword())) {
            throw new IllegalActionException("Password does not meet security requirements");
        }

        User authenticatedUser = getAuthenticatedUser();
        User user = getUserEntity(passwordResetRequest.userId());

        if (!isSameUser(user.getId(), authenticatedUser.getId())) {
            throw new IllegalActionException("You can't reset another user's account password");
        }

        String newPassword = passwordResetRequest.newPassword();
        String confirmedPassword = passwordResetRequest.confirmedPassword();

        if (!confirmedPassword.equals(newPassword)) {
            throw new IllegalActionException("The two passwords aren't identical");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return user.getId();
    }

    @Transactional
    @Override
    public Long changeUserAccountPassword(PasswordChangeRequest passwordChangeRequest) {
        if (!PasswordValidationHelper.isValid(passwordChangeRequest.newPassword())) {
            throw new IllegalActionException("Password does not meet security requirements");
        }

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