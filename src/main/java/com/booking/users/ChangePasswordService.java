package com.booking.users;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.view.model.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ChangePasswordService {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";
    private final UserRepository userRepository;

    public ChangePasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(ChangePasswordRequest changePasswordRequest, String userName) throws ChangePasswordException {
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ChangePasswordException("New password does not match confirm new password");
        }
        if (!Pattern.matches(PASSWORD_PATTERN, newPassword)) {
            throw new ChangePasswordException("New password does not match the pattern");
        }
        if (changePasswordRequest.getOldPassword().equals(newPassword)) {
            throw new ChangePasswordException("New password is same as old password");
        }
        User user = userRepository.findByUsername(userName).get();
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            throw new ChangePasswordException("Old password is wrong");
        }

        user.setPassword(newPassword);

        userRepository.save(user);
    }

}
