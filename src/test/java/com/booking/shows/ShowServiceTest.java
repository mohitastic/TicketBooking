package com.booking.shows;

import com.booking.exceptions.ShowException;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.shows.view.models.ShowRequest;
import com.booking.slots.repository.Slot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ShowServiceTest {
    private ShowRepository showRepository;
    private MovieGateway movieGateway;

    @BeforeEach
    public void beforeEach() {
        showRepository = mock(ShowRepository.class);
        movieGateway = mock(MovieGateway.class);
    }

    @Test
    public void should_get_movie_by_id() throws IOException, FormatException {
        Movie movie = mock(Movie.class);
        when(movieGateway.getMovieFromId("movie_1")).thenReturn(movie);
        ShowService showService = new ShowService(showRepository, movieGateway);

        showService.getMovieById("movie_1");

        verify(movieGateway).getMovieFromId("movie_1");
    }

    @Test
    public void should_fetch_all_shows_and_set_movie_gateway() {
        List<Show> shows = new ArrayList<>();
        Slot slotOne = new Slot();
        Slot slotTwo = new Slot();
        Date date = Date.valueOf("2020-01-01");

        shows.add(new Show(Date.valueOf("2020-01-01"), slotOne, new BigDecimal("299.99"), "movie_1"));

        shows.add(new Show(Date.valueOf("2020-01-01"), slotTwo, new BigDecimal("299.99"), "movie_1"));

        when(showRepository.findByDate(date)).thenReturn(shows);
        ShowService showService = new ShowService(showRepository, movieGateway);

        List<Show> actualShows = showService.fetchAll(date);

        List<Show> expectedShows = new ArrayList<>();
        expectedShows.add(new Show(Date.valueOf("2020-01-01"), slotOne, new BigDecimal("299.99"), "movie_1"));

        expectedShows.add(new Show(Date.valueOf("2020-01-01"), slotTwo, new BigDecimal("299.99"), "movie_1"));

        assertThat(actualShows, is(equalTo(expectedShows)));
    }

//    @Test
//    void should_be_able_to_add_new_show() throws ShowException, IOException, FormatException {
//        Slot slotOne = new Slot();
//        Date date = Date.valueOf("2022-04-25");
//        ShowService showService = new ShowService(showRepository, movieGateway);
//        ShowRequest showRequest = new ShowRequest(date, slotOne, new BigDecimal("299.99"), "movie_1");
//
//        showService.addShow(showRequest);
//
//        verify(showRepository).save(new Show(showRequest.getDate(), showRequest.getSlot(), showRequest.getCost(), showRequest.getMovieId()));
//    }

    @Test
    void should_Throw_An_Exception_When_Try_To_Add_Past_Show() {
        Slot slotOne = new Slot();
        Date date = Date.valueOf("2020-01-01");
        ShowService showService = new ShowService(showRepository, movieGateway);
        ShowRequest showRequest = new ShowRequest(date, slotOne, new BigDecimal("299.99"), "movie_1");
        String expected = "400 : Past date not allowed";

        ShowException showException = assertThrows(ShowException.class, () -> showService.addShow(showRequest));
        assertEquals(expected, showException.getMessage());
    }

    @Test
    void should_Throw_An_Exception_When_Try_To_Add_Show_With_NonExistent_MovieId() {
        Slot slotOne = new Slot();
        Date date = Date.valueOf(LocalDate.now());
        ShowService showService = new ShowService(showRepository, movieGateway);
        ShowRequest showRequest = new ShowRequest(date, slotOne, new BigDecimal("299.99"), "movie_1");

        String expected = "404 : Movie Id not exist";

        ShowException showException = assertThrows(ShowException.class, () -> showService.addShow(showRequest));
        assertEquals(expected, showException.getMessage());
    }
}
