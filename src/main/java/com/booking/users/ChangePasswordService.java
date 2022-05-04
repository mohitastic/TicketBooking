package com.booking.users;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.view.model.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.regex.Pattern;

@Service
@Configurable
public class ChangePasswordService {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";
    private final UserRepository userRepository;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    public ChangePasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(ChangePasswordRequest changePasswordRequest, String userName) throws ChangePasswordException {
        String newPassword = changePasswordRequest.getNewPassword();
        String confirmNewPassword = changePasswordRequest.getConfirmNewPassword();

        newPasswordNotEqualToConfirmNewPassword(newPassword, confirmNewPassword);

        newPasswordNotMathcesWithPattern(newPassword);

        newPasswordShouldNotBeEqualToOldPassword(changePasswordRequest, newPassword);

        User user = userRepository.findByUsername(userName).get();

        oldPasswordNotMatchesWithUserPassword(user, changePasswordRequest);

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncodedPassword);

        userRepository.save(user);

    }

    private void oldPasswordNotMatchesWithUserPassword(User user, ChangePasswordRequest changePasswordRequest) throws ChangePasswordException {
        String oldPassword = changePasswordRequest.getOldPassword();
        String userPassword = user.getPassword();
        if (!passwordEncoder.matches(oldPassword, userPassword)) {
            throw new ChangePasswordException("Old password is wrong");
        }
    }

    private void newPasswordShouldNotBeEqualToOldPassword(ChangePasswordRequest changePasswordRequest, String newPassword) throws ChangePasswordException {
        if (changePasswordRequest.getOldPassword().equals(newPassword)) {
            throw new ChangePasswordException("New password is same as old password");
        }
    }

    private void newPasswordNotMathcesWithPattern(String newPassword) throws ChangePasswordException {
        if (!Pattern.matches(PASSWORD_PATTERN, newPassword)) {
            throw new ChangePasswordException("New password does not match the pattern");
        }
    }

    void newPasswordNotEqualToConfirmNewPassword(String newPassword, String confirmNewPassword) throws ChangePasswordException {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new ChangePasswordException("New password does not match confirm new password");
        }
    }

}
