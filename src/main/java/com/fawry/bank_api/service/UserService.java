package com.fawry.bank_api.service;

import com.fawry.bank_api.dto.user.PasswordChangeRequest;
import com.fawry.bank_api.dto.user.PasswordResetRequest;
import com.fawry.bank_api.dto.user.UserDetailsResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    //admin authorities only

    List<UserDetailsResponse> findAllUsers();
    List<UserDetailsResponse> getUsersWithActiveAccounts();
  List<UserDetailsResponse> getUsersWithUnActiveAccounts();
  Long activateUser(Long userId);

    Long deactivateUser(Long userId);



    //common user activities
    Long resetUserAccountPassword(PasswordResetRequest passwordResetRequest);
    Long changeUserAccountPassword(PasswordChangeRequest passwordChangeRequest);
    UserDetailsResponse getUserProfile(Long userId);

}
