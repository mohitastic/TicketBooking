package com.booking.users;

import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserPrincipalServiceTest {

    @Test
    void shouldEncodePasswordWhenPasswordDoesNotMatchThePattern(){
        String username = "test-user";
        User user = new User(username, "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        mockUserRepository.save(user);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));
        when(userPrincipalService.findUserByUsername(username)).thenReturn(user);

        assertDoesNotThrow(()->userPrincipalService.loadUserByUsername(username));

    }

    @Test
    void shouldThrowUserNotFoundException() {
        String username = "test-user";
        User user = new User(username, "Password@1");
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserPrincipalService userPrincipalService = new UserPrincipalService(mockUserRepository);
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));
        String expectedExceptionMessage="User not found";

        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> userPrincipalService.loadUserByUsername(username));

        assertEquals(usernameNotFoundException.getMessage(), expectedExceptionMessage);

    }

}