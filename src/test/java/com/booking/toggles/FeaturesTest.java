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

}