package com.fawry.bank_api.util;
public class PasswordValidationHelper {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,}$";

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}