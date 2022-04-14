package com.booking.users;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.view.model.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ChangePasswordService {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";

    public void execute(ChangePasswordRequest changePasswordRequest) throws ChangePasswordException {
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();
        if(!newPassword.equals(confirmNewPassword)) {
            throw new ChangePasswordException("New password does not match confirm new password");
        }
        if(!Pattern.matches(PASSWORD_PATTERN, newPassword)) {
            throw new ChangePasswordException("New password does not match the pattern");
        }
    }
}
