package com.booking.users;

import com.booking.exceptions.UserSignUpException;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetails;
import com.booking.users.view.model.UserSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSignUpServiceTest {
    private UserRepository mockUserRepository;
    private UserDetailsRepository mockUserDetailsRepository;

    @BeforeEach
    public void beforeEach() {
        mockUserRepository = mock(UserRepository.class);
        mockUserDetailsRepository = mock(UserDetailsRepository.class);
    }

    @Test
    void shouldHaveValuesForAllFieldsInTheSignUp() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bac@gmail.com", "", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "The fields cannot be empty.";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordDoesNotMatchConfirmPassword() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "Password@1", "Password@2");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Password does not match Confirm Password";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenNamedPatternIsNotFollowed() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc12", "abc", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Name does not match the pattern";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordPatternIsNotFollowed() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "password@1", "password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Password does not match the pattern";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenThePhoneNumberDoesNotHaveTenDigits() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bac@gmail.com", "123456789", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Phone number is not valid";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenTheEmailIsInvalid() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bacgmail@.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Email address is not valid";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenUserNameAlreadyExists() {
        String username = "test-user";
        User user = new User(username, "correct-password");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "test-user", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "User name already exists";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void shouldFailWhenPhoneNumberAlreadyExists() {
        UserDetails userDetails = new UserDetails("abc", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", new User("test", "Password@1"));
        mockUserDetailsRepository.save(userDetails);
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "test-user", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);
        String expectedExceptionMessage = "Phone Number already exists";

        when(mockUserDetailsRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.ofNullable(userDetails));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
        verify(mockUserDetailsRepository).findByPhoneNumber("1234567890");
    }

    @Test
    void shouldPassWhenSignUpIsSuccessful() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", new Date(1996, 04, 19), "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository);

        when(mockUserRepository.findByUsername("abc")).thenReturn(Optional.ofNullable(null));

        assertDoesNotThrow(() -> userSignUpService.execute(userSignUpRequest));
        verify(mockUserRepository).findByUsername("abc");
    }
}
