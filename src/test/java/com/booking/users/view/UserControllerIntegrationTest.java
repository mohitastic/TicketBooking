package com.booking.users.view;

import com.booking.App;
import com.booking.exceptions.ChangePasswordException;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

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
        userRepository.save(new User("test-user", "password"));
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
    void shouldChangePasswordSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password"));
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
    void shouldChangePasswordUnSuccessfully() throws Exception {
        userRepository.save(new User("test-user", "password"));
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
