package com.booking.bookings.view;

import com.booking.App;
import com.booking.bookings.repository.BookingRepository;
import com.booking.customers.repository.CustomerRepository;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import com.booking.toggles.Features;
import com.booking.users.Role;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;

import static com.booking.shows.respository.Constants.MAX_NO_OF_SEATS_PER_BOOKING;
import static com.booking.users.Role.Code.ADMIN;
import static com.booking.users.Role.Code.CUSTOMER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(roles = ADMIN)
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @MockBean
    private MovieGateway movieGateway;
    private Show showOne;

    @BeforeEach
    public void beforeEach() throws IOException, FormatException {
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        slotRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
        userDetailsRepository.deleteAll();

        when(movieGateway.getMovieFromId("movie_1"))
                .thenReturn(
                        new Movie(
                                "movie_1",
                                "Movie name",
                                Duration.ofHours(1).plusMinutes(30),
                                "Movie description",
                                "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg",
                                "7.2"
                        )
                );

        userRepository.save(new User("testUser1", "Foobar@1", CUSTOMER));
        userDetailsRepository.save(new UserDetail("John",Date.valueOf("2022-04-08"),"abc@gmail.com","9843890977",new User("testUser2", "foobar", CUSTOMER)));
        Slot slotOne = slotRepository.save(new Slot("Test slot", Time.valueOf("09:30:00"), Time.valueOf("12:00:00")));
        showOne = showRepository.save(new Show(Date.valueOf("2020-01-01"), slotOne, new BigDecimal("249.99"), "movie_1"));
    }

    @AfterEach
    public void afterEach() {
        bookingRepository.deleteAll();
        showRepository.deleteAll();
        slotRepository.deleteAll();
        customerRepository.deleteAll();
        userDetailsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void should_save_booking_walkIn_customer_and_customer_detail() throws Exception {
        final String requestJson = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"customer\": " + "{\"name\": \"Customer\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": 2" +
                "}";

        mockMvc.perform(post("/bookings/walkInCustomer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(content().json("{" +
                        "\"customerName\":\"Customer\"," +
                        "\"showDate\":\"2020-01-01\"," +
                        "\"startTime\":\"09:30:00\"," +
                        "\"amountPaid\":499.98," +
                        "\"noOfSeats\":2}"));

        assertThat(customerRepository.findAll().size(), is(1));
        assertThat(bookingRepository.findAll().size(), is(1));
    }

    @WithMockUser
    @Test
    void should_not_book_when_user_is_not_admin() throws Exception {
        final String requestJson = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"customer\": " + "{\"name\": \"Customer\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": 2" +
                "}";

        mockMvc.perform(post("/bookings/walkInCustomer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = CUSTOMER)
    @Test
    @AllEnabled(Features.class)
    public void should_save_booking_user_customer_when_is_enabled() throws Exception {
        final String requestJson = "{" +
                "\"date\": \"2020-06-04\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"username\": \"testUser2\"," +
                "\"noOfSeats\": 2" +
                "}";

        mockMvc.perform(post("/bookings/userCustomer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(content().json("{" +
                        "\"customerName\":\"testUser2\"," +
                        "\"showDate\":\"2020-01-01\"," +
                        "\"startTime\":\"09:30:00\"," +
                        "\"amountPaid\":499.98," +
                        "\"noOfSeats\":2}"));


        assertThat(bookingRepository.findAll().size(), is(1));
    }

    @Test
    @AllDisabled(Features.class)
    @WithMockUser(roles = CUSTOMER)
    public void should_not_save_booking_user_customer_when_feature_is_disabled() throws Exception {
        final String requestJson = "{" +
                "\"date\": \"2020-06-04\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"username\": \"testUser2\"," +
                "\"noOfSeats\": 2" +
                "}";

        mockMvc.perform(post("/bookings/userCustomer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void should_not_book_when_seats_booking_is_greater_than_allowed() throws Exception {
        final String moreThanAllowedSeatsRequestJson = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"customer\": " + "{\"name\": \"Customer\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": " + (Integer.parseInt(MAX_NO_OF_SEATS_PER_BOOKING) + 1) +
                "}";


        mockMvc.perform(post("/bookings/walkInCustomer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(moreThanAllowedSeatsRequestJson))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void should_not_book_when_max_capacity_for_seats_exceeds() throws Exception {
        setupBookingSeatsForSameShow();

        final String overCapacityRequest = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"customer\": " + "{\"name\": \"Customer 1\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": 11" +
                "}";

        mockMvc.perform(post("/bookings/walkInCustomer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(overCapacityRequest))
                .andExpect(status().is5xxServerError())
                .andReturn();

    }

    private void setupBookingSeatsForSameShow() throws Exception {
        final String successRequest = "{" +
                "\"date\": \"2020-06-01\"," +
                "\"showId\": " + showOne.getId() + "," +
                "\"customer\": " + "{\"name\": \"Customer\", \"phoneNumber\": \"9922334455\"}," +
                "\"noOfSeats\": " + MAX_NO_OF_SEATS_PER_BOOKING +
                "}";

        for (int i = 0; i < 6; i++) { // simulate booking for 90 seats for a same show
            mockMvc.perform(post("/bookings/walkInCustomer")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(successRequest))
                    .andExpect(status().isCreated())
                    .andReturn();
        }
    }
}
