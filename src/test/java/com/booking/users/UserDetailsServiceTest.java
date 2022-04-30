package com.booking.users;

import com.booking.exceptions.UserDetailNotFoundException;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceTest {

    private UserRepository mockUserRepository;
    private UserDetailsRepository mockUserDetailsRepository;

    @BeforeEach
    public void beforeEach() {
        mockUserRepository = mock(UserRepository.class);
        mockUserDetailsRepository = mock(UserDetailsRepository.class);
    }

    @Test
    void shouldBeAbleToViewUserDetails() throws UserDetailNotFoundException {
        Date date = Date.valueOf("1996-04-19");
        User user = new User(1L,"abc", "Password@1");
        UserDetail userDetails = new UserDetail("abc", date, "bac@email.com", "1234567890", user);
        mockUserRepository.save(user);
        mockUserDetailsRepository.save(userDetails);
        when(mockUserRepository.findByUsername("abc")).thenReturn(Optional.ofNullable(user));
        UserDetailService userDetailsService = new UserDetailService(mockUserRepository, mockUserDetailsRepository);

        assertThrows(UserDetailNotFoundException.class,() -> userDetailsService.fetch("abc"));
    }

}