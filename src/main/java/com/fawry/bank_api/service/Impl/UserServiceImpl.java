package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import com.fawry.bank_api.entity.PasswordChangeRequests;
import com.fawry.bank_api.entity.User;
import com.fawry.bank_api.enums.UserRole;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.mapper.UserMapper;
import com.fawry.bank_api.repository.PasswordChangeRequestsRepository;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.service.UserService;
import com.fawry.bank_api.util.PasswordValidationHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;

        this.userMapper = userMapper;
    }

    @Override
    public List<UserDetailsResponse> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public List<UserDetailsResponse> getUsersWithActiveAccounts() {
        return userRepository.getUsersWithActiveAccounts().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public List<UserDetailsResponse> getUsersWithUnActiveAccounts() {
        return userRepository.getUsersWithUnActiveAccounts().stream().map(userMapper::toUserResponse).toList();
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
    public Long activateUser(Long userId) {
        User user = getUserEntity(userId);
        User authenticatedUser = getAuthenticatedUser();

        if (isSameUser(userId, authenticatedUser.getId())) {
            throw new IllegalActionException("You can't activate your account");
        }

        user.setIsActive(true);
        return user.getId();
    }

    @Transactional
    @Override
    public Long deactivateUser(Long userId) {
        User authenticatedUser = getAuthenticatedUser();
        User user = getUserEntity(userId);

        if (isSameUser(userId, authenticatedUser.getId())) {
            throw new IllegalActionException("You can't deactivate your account");
        }

        user.setIsActive(false);
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