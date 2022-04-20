package com.booking.users.view;

import com.booking.App;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser
class UserSignUpControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @BeforeEach
    public void before() {
        userDetailsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        userDetailsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldSignUpSuccessfully() throws Exception {
        final String bodyJson = "{" +
                "\"name\": \"user4\"," +
                "\"username\": \"user4\"," +
                "\"dob\": \"1996-04-19\"," +
                "\"email\": \"user@email.com\"," +
                "\"phoneNumber\": \"1234567899\"," +
                "\"password\": \"Password@4\"," +
                "\"confirmPassword\": \"Password@4\"" +
                "}";

        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(bodyJson)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailWhenTheRequestIsBad() throws Exception {
        final String bodyJson = "{" +
                "\"name\": \"user4\"," +
                "\"username\": \"user4\"," +
                "\"dob\": \"1996-04-19\"," +
                "\"email\": \"useremail.com\"," +
                "\"phoneNumber\": \"1234567899\"," +
                "\"password\": \"Password@4\"," +
                "\"confirmPassword\": \"Password@4\"" +
                "}";

        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(bodyJson)
                )
                .andExpect(status().isBadRequest());
    }
}