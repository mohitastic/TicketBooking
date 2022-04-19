package com.booking.users;

import com.booking.exceptions.*;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.view.model.UserSignUpRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserSignUpServiceTest {
    @Test
    void shouldHaveValuesForAllFieldsInTheSignUp() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bac@gmail.com", "","Password@1", "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "The fields cannot be empty.";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordDoesNotMatchConfirmPassword() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bac@gmail.com", "1234567890","Password@1", "Password@2");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "Password does not match Confirm Password";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordPatternIsNotFollowed() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bac@gmail.com", "1234567890","password@1", "password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "Password does not match the pattern";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenThePhoneNumberDoesNotHaveTenDigits() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bac@gmail.com", "123456789","Password@1", "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "Phone number is not valid";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenTheEmailIsInvalid() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bacgmail.com", "1234567890","Password@1", "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "Email address is not valid";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenUserNameAlreadyExists() {
        String username = "test-user";
        User user = new User(username, "correct-password");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "test-user", "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);
        String expectedExceptionMessage = "User name already exists";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void shouldPassWhenSignUpIsSuccessful() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository);

        when(mockUserRepository.findByUsername("abc")).thenReturn(Optional.ofNullable(null));

        assertDoesNotThrow(() -> userSignUpService.execute(userSignUpRequest));
        verify(mockUserRepository).findByUsername("abc");
    }
}
