package com.booking.movie.view;

import com.booking.App;
import com.booking.movie.MovieService;
import com.booking.movieGateway.models.Movie;
import com.booking.toggles.Features;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.booking.users.Role.Code.ADMIN;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(username = "testuser", password = "Password@2", roles = ADMIN)
class MovieControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieService movieGateway;

    @Test
    @AllEnabled(Features.class)
    public void retrieveAllExistingMoviesWhenFeatureIsEnabled() throws Exception {
        Movie movie1 = new Movie("tt6644200", "movie1", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        Movie movie2 = new Movie("tt6857112", "movie2", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"tt6644200\",\"name\":\"movie1\",\"duration\":\"1h 30m\",\"plot\":\"description\",\"posterLink\":\"link\",\"imdbRating\":\"6.3\"}," +
                        "{\"id\":\"tt6857112\",\"name\":\"movie2\",\"duration\":\"1h 30m\",\"plot\":\"description\",\"posterLink\":\"link\",\"imdbRating\":\"6.3\"}]"));
    }

    @AllEnabled(Features.class)
    @WithMockUser
    @Test
    void shouldNotRetrieveAllExistingMoviesWhenUserIsNotAdminAndFeatureIsEnabled() throws Exception {
        Movie movie1 = new Movie("tt6644200", "movie1", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        Movie movie2 = new Movie("tt6857112", "movie2", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isForbidden());
    }

    @Test
    @AllDisabled(Features.class)
    public void shouldNotRetrieveMoviesWhenFeatureIsDisabled() throws Exception {
        Movie movie1 = new Movie("tt6644200", "movie1", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        Movie movie2 = new Movie("tt6857112", "movie2", Duration.ofHours(1).plusMinutes(30), "description", "link", "6.3");
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isBadRequest());
    }
}
