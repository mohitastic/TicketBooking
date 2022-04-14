package com.booking.users;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.view.model.ChangePasswordRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordServiceTest {
    @Test
    void shouldFailWhenNewPasswordDoesNotMatchConfirmNewPassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password", "NewPassword", "DifferentPassword");
        ChangePasswordService changePasswordService = new ChangePasswordService();
        String expectedExceptionMessage = "New password does not match confirm new password";

        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordPatternIsNotFollowed() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password", "NewPassword", "NewPassword");
        ChangePasswordService changePasswordService = new ChangePasswordService();
        String expectedExceptionMessage = "New password does not match the pattern";

        ChangePasswordException changePasswordException = assertThrows(ChangePasswordException.class, () -> changePasswordService.execute(changePasswordRequest));
        assertEquals(changePasswordException.getMessage(), expectedExceptionMessage);
    }
}