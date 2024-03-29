package com.booking.shows.view;

import com.booking.App;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import com.booking.toggles.Features;
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
import org.togglz.junit5.AllEnabled;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.booking.users.Role.Code.ADMIN;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(roles = ADMIN)
public class ShowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SlotRepository slotRepository;

    @MockBean
    private MovieGateway movieGateway;

    @BeforeEach
    public void before() {
        showRepository.deleteAll();
        slotRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        showRepository.deleteAll();
        slotRepository.deleteAll();
    }

    @Test
    public void retrieveAllExistingShows() throws Exception {
        when(movieGateway.getMovieFromId("movie_1"))
                .thenReturn(
                        new Movie(
                                "movie_1",
                                "Movie name",
                                Duration.ofHours(1).plusMinutes(30),
                                "Movie plot",
                                "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg",
                                "6.3"
                        )
                );
        final Slot slotOne = slotRepository.save(new Slot("Test slot one", Time.valueOf("09:30:00"), Time.valueOf("12:00:00")));
        final Slot slotTwo = slotRepository.save(new Slot("Test slot two", Time.valueOf("13:30:00"), Time.valueOf("16:00:00")));
        final Show showOne = showRepository.save(new Show(Date.valueOf("2020-01-01"), slotOne, new BigDecimal("249.99"), "movie_1"));
        final Show showTwo = showRepository.save(new Show(Date.valueOf("2020-01-01"), slotTwo, new BigDecimal("299.99"), "movie_1"));
        showRepository.save(new Show(Date.valueOf("2020-01-02"), slotOne, new BigDecimal("249.99"), "movie_1"));

        mockMvc.perform(get("/shows?date=2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[" +
                                "{'id':" + showOne.getId() + ",'date':'2020-01-01','cost':249.99," +
                                "'slot':{'id':" + slotOne.getId() + ",'name':'Test slot one','startTime':'9:30 AM','endTime':'12:00 PM'}," +
                                "'movie':{'id':'movie_1','name':'Movie name','duration':'1h 30m','plot':'Movie plot'}}," +
                                "{'id':" + showTwo.getId() + ",'date':'2020-01-01','cost':299.99," +
                                "'slot':{'id':" + slotTwo.getId() + ",'name':'Test slot two','startTime':'1:30 PM','endTime':'4:00 PM'}," +
                                "'movie':{'id':'movie_1','name':'Movie name','duration':'1h 30m','plot':'Movie plot'}}" +
                                "]"));
    }

    @Test
    @AllEnabled(Features.class)
    void shouldNotAddTheShowWhenFeatureIsDisabled() throws Exception {
        Slot slotOne = new Slot("slot1", Time.valueOf(LocalTime.now().plusMinutes(5)), Time.valueOf(LocalTime.now().plusMinutes(20)));
        slotRepository.save(slotOne);
        List<Slot> slots = slotRepository.findAll();

        mockMvc.perform(
                        post("/shows")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content("{\"cost\":299.99,\"date\":\"2022-04-01\",\"movieId\":\"movie_1\",\"slotId\":" + slots.get(0).getId() + "}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @AllEnabled(Features.class)
    void shouldAddTheShowSuccessfullyWhenFeatureEnabled() throws Exception {
        Date date = Date.valueOf(LocalDate.now());
        Slot slotOne = new Slot("slot1", Time.valueOf(LocalTime.now().plusMinutes(5)), Time.valueOf(LocalTime.now().plusMinutes(20)));
        slotRepository.save(slotOne);
        List<Slot> slots = slotRepository.findAll();

        mockMvc.perform(
                        post("/shows")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content("{\"cost\":299.99,\"date\":\"" + date + "\",\"movieId\":\"movie_1\",\"slotId\":" + slots.get(0).getId() + "}")
                )
                .andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    @AllEnabled(Features.class)
    void shouldNotAddShowSuccessfullyWhenUserIsNotAdminAndFeatureIsEnabled() throws Exception {
        Date date = Date.valueOf(LocalDate.now());
        Slot slotOne = new Slot("slot1", Time.valueOf(LocalTime.now().plusMinutes(5)), Time.valueOf(LocalTime.now().plusMinutes(20)));
        slotRepository.save(slotOne);
        List<Slot> slots = slotRepository.findAll();

        mockMvc.perform(
                        post("/shows")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content("{\"cost\":299.99,\"date\":\"" + date + "\",\"movieId\":\"movie_1\",\"slotId\":" + slots.get(0).getId() + "}")
                )
                .andExpect(status().isForbidden());
    }
}
