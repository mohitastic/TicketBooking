package com.booking.movie;

import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class MovieServiceTest {
    private MovieGateway movieGateway;

    @Autowired
    private MovieService movieService;

    @BeforeEach
    public void beforeEach() {
        movieGateway = mock(MovieGateway.class);
        movieService = new MovieService(movieGateway);
    }

    @Test
    public void shouldReturnAllMovies() throws IOException {
        Movie mockMovie = mock(Movie.class);
        List<Movie> movies = new ArrayList<>();
        movies.add(mockMovie);
        when(movieGateway.getAllMovies()).thenReturn(movies);

        movieService.getAllMovies();

        verify(movieGateway).getAllMovies();
    }

}