package com.booking.movieGateway.models;

import com.booking.movieGateway.exceptions.FormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieServiceResponseTest {

    @Test
    public void should_convert_to_a_movie_with_valid_runtime() throws FormatException {
        final var movieServiceResponse = new MovieServiceResponse("id", "title", "50 min", "plot","https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg", "7.2");

        final var actualMovie = movieServiceResponse.toMovie();

        final var expectedMovie = new Movie("id", "title", Duration.ofMinutes(50), "plot","https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg", "7.2");
        assertThat(actualMovie, is(equalTo(expectedMovie)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "NaN min"})
    public void should_not_convert_to_a_movie_with_invalid_runtime(String runtime) {
        final var movieServiceResponse = new MovieServiceResponse("id", "title", runtime, "plot", "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg", "5.7");

        final var formatException = assertThrows(FormatException.class, movieServiceResponse::toMovie);
        assertThat(formatException.getMessage(), is(equalTo("runtime has an illegal format")));
    }
}
