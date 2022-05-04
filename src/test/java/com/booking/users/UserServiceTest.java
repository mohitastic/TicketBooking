package com.booking.users;

import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.booking.users.Role.Code.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserRepository mockUserRepository;

    @BeforeEach
    public void beforeEach() {
        mockUserRepository = mock(UserRepository.class);
    }

    @Test
    void shouldReturnUserRoleWhenProperUsernameIsPassed() {
        User user = new User(1L,"testuser", "Password@1", CUSTOMER);
        mockUserRepository.save(user);
        UserService userService = new UserService(mockUserRepository);
        when(mockUserRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        String userRole = userService.fetchRole(user.getUsername());
        assertEquals("CUSTOMER", userRole);
    }

    @Test
    void shouldFailWhenProperUsernameIsNotPassed() {
        User user = new User(1L,"testuser", "Password@1", CUSTOMER);
        mockUserRepository.save(user);
        UserService userService = new UserService(mockUserRepository);
        when(mockUserRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class, () -> userService.fetchRole("abc"));
    }
}