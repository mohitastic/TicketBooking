package com.booking.users;

import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.exceptions.UserSignUpException;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import com.booking.users.view.model.UserSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static com.booking.users.Role.Code.CUSTOMER;
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
        Date date = Date.valueOf("1996-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bac@gmail.com", "", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "The fields cannot be empty.";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordDoesNotMatchConfirmPassword() {
        Date date = Date.valueOf("1996-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bac@gmail.com", "1234567890", "Password@1", "Password@2");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Password does not match Confirm Password";

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenNamePatternIsNotFollowed() {
        Date date = Date.valueOf("1996-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc12", "abc", date, "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Name does not match the pattern";

        PatternDoesNotMatchException patternDoesNotMatchException = assertThrows(PatternDoesNotMatchException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(patternDoesNotMatchException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenPasswordPatternIsNotFollowed() {
        Date date = Date.valueOf("2020-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bac@gmail.com", "1234567890", "password@1", "password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Password does not match the pattern";

        PatternDoesNotMatchException patternDoesNotMatchException = assertThrows(PatternDoesNotMatchException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(patternDoesNotMatchException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenThePhoneNumberDoesNotHaveTenDigits() {
        Date date = Date.valueOf("1996-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bac@gmail.com", "123456789", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Phone number is not valid";

        PatternDoesNotMatchException patternDoesNotMatchException = assertThrows(PatternDoesNotMatchException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(patternDoesNotMatchException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenTheEmailIsInvalid() {
        Date date = Date.valueOf("1996-04-19");
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bacgmail@.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Email address is not valid";

        PatternDoesNotMatchException patternDoesNotMatchException = assertThrows(PatternDoesNotMatchException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(patternDoesNotMatchException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenUserEntersFutureDateOfBirth() {
        String username = "test-user";
        User user = new User(username, "correct-password", CUSTOMER);
        Date date = Date.valueOf(java.time.LocalDate.now().plusDays(2));
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abcde", "test-userrr", date, "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Invalid Date Of Birth";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
    }

    @Test
    void shouldFailWhenUserNameAlreadyExists() {
        Date date = Date.valueOf("1996-04-19");
        String username = "test-user";
        User user = new User(username, "correct-password", CUSTOMER);
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "test-user", date, "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "User name already exists";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
        verify(mockUserRepository).findByUsername(username);
    }

    @Test
    void shouldFailWhenPhoneNumberAlreadyExists() {
        Date date = Date.valueOf("1996-04-19");
        UserDetail userDetails = new UserDetail("abc", date, "bac@gmail.com", "1234567890", new User("test", "Password@1", CUSTOMER));
        mockUserDetailsRepository.save(userDetails);
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "test-user", date, "bac@gmail.com", "1234567890", "Password@1", "Password@1");
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository, new BCryptPasswordEncoder());
        String expectedExceptionMessage = "Phone Number already exists";

        when(mockUserDetailsRepository.findByPhoneNumber("1234567890")).thenReturn(Optional.ofNullable(userDetails));

        UserSignUpException userSignUpException = assertThrows(UserSignUpException.class, () -> userSignUpService.execute(userSignUpRequest));
        assertEquals(userSignUpException.getMessage(), expectedExceptionMessage);
        verify(mockUserDetailsRepository).findByPhoneNumber("1234567890");
    }

    @Test
    void shouldPassWhenSignUpIsSuccessful() {
        Date date = Date.valueOf("1996-04-19");
        String password = "Password@1";
        System.out.println(password);
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("abc", "abc", date, "bac@gmail.com", "1234567890", password, password);
        UserSignUpService userSignUpService = new UserSignUpService(mockUserRepository, mockUserDetailsRepository,new BCryptPasswordEncoder());

        when(mockUserRepository.findByUsername("abc")).thenReturn(Optional.ofNullable(null));

        assertDoesNotThrow(() -> userSignUpService.execute(userSignUpRequest));
        verify(mockUserRepository).findByUsername("abc");
    }
}
