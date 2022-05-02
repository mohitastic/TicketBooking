package com.booking.users.view;

import com.booking.App;
import com.booking.exceptions.ChangePasswordException;
import com.booking.toggles.Features;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

import static com.booking.users.Role.Code.CUSTOMER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserControllerIntegrationTest {

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
    public void shouldLoginSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password", CUSTOMER));
        mockMvc.perform(
                        get("/login")
                                .with(httpBasic("test-user", "password"))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowErrorMessageForInvalidCredentials() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @AllEnabled(Features.class)
    void shouldChangePasswordSuccessfullyWhenFeatureIsEnabled() throws Exception {
        userRepository.save(new User("test-user", "password", CUSTOMER));
        final String bodyJson = "{" +
                "\"oldPassword\": \"password\"," +
                "\"newPassword\": \"Password@1\"," +
                "\"confirmNewPassword\": \"Password@1\"" +
                "}";

        mockMvc.perform(
                        put("/changePassword")
                                .with(httpBasic("test-user", "password"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(bodyJson)
                )
                .andExpect(status().isOk());
    }

    @Test
    @AllEnabled(Features.class)
    void shouldChangePasswordUnSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password", CUSTOMER));
        final String bodyJson = "{" +
                "\"oldPassword\": \"password1\"," +
                "\"newPassword\": \"Password@1\"," +
                "\"confirmNewPassword\": \"Password@1\"" +
                "}";

        mockMvc.perform(
                        put("/changePassword")
                                .with(httpBasic("test-user", "password"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(bodyJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @AllDisabled(Features.class)
    void shouldNotChangePasswordWhenFeatureIsDisabled() throws Exception {
        userRepository.save(new User("test-user", "password", CUSTOMER));
        final String bodyJson = "{" +
                "\"oldPassword\": \"password\"," +
                "\"newPassword\": \"Password@1\"," +
                "\"confirmNewPassword\": \"Password@1\"" +
                "}";

        mockMvc.perform(
                        put("/changePassword")
                                .with(httpBasic("test-user", "password"))
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(bodyJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @AllEnabled(Features.class)
    void shouldSignUpSuccessfullyWhenFeatureIsEnabled() throws Exception {
        final String bodyJson = "{" +
                "\"name\": \"user\"," +
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
    @AllEnabled(Features.class)
    void shouldFailWhenTheRequestIsBadAndFeatureIsEnabled() throws Exception {
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

    @Test
    @AllDisabled(Features.class)
    void shouldNotSignUpSuccessfullyWhenFeatureIsDisabled() throws Exception {
        final String bodyJson = "{" +
                "\"name\": \"user\"," +
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
                .andExpect(status().isBadRequest());
    }
}
