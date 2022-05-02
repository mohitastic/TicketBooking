package com.booking.toggles;

import com.booking.App;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class)
public class FeaturesTest {
    @Test
    @AllEnabled(Features.class)
    void allEnabled() {
        assertTrue(Features.MOVIE_SCHEDULE.isActive());
        assertTrue(Features.VIEW_USER_PROFILE.isActive());
        assertTrue(Features.CUSTOMER_SIGN_UP.isActive());
        assertTrue(Features.CHANGE_PASSWORD.isActive());
    }

    @Test
    @AllDisabled(Features.class)
    void allDisabled() {
        assertFalse(Features.MOVIE_SCHEDULE.isActive());
        assertFalse(Features.VIEW_USER_PROFILE.isActive());
        assertFalse(Features.CUSTOMER_SIGN_UP.isActive());
        assertFalse(Features.CHANGE_PASSWORD.isActive());
    }
}