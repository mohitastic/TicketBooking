package com.booking.movieGateway;

import com.booking.config.AppConfig;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMovieGatewayTest {

    public static final String MOCK_SERVER_SINGLE_RESPONSE = "{\"imdbID\":\"id\"," + "\"Title\":\"title\"," + "\"Runtime\":\"120 min\"," + "\"Plot\":\"plot\"}";

    public static final String MOCK_SERVER_MULTIPLE_RESPONSES = "[{\"imdbID\":\"id1\"," + "\"Title\":\"title\"," + "\"Runtime\":\"120 min\"," + "\"Plot\":\"plot\"}," + "{\"imdbID\":\"id2\"," + "\"Title\":\"title\"," + "\"Runtime\":\"120 min\"," + "\"Plot\":\"plot\"}]";

    public static final String MOCK_SERVER_RESPONSE = "{\"imdbID\":\"id\"," +
            "\"Title\":\"title\"," +
            "\"Runtime\":\"120 min\"," +
            "\"Plot\":\"plot\"," +
            "\"imdbRating\":\"8.2\"}" ;
    private MockWebServer mockWebServer;


    public void setUpMockServer(String mockResponse) throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(mockResponse));
        mockWebServer.start();
    }

    @Test
    public void should_get_movie_from_service() throws IOException, FormatException {
        setUpMockServer(MOCK_SERVER_SINGLE_RESPONSE);
        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(String.valueOf(mockWebServer.url("/")));

        final var defaultMovieGateway = new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(), new ObjectMapper());

        final var actualMovie = defaultMovieGateway.getMovieFromId("id1");

        final var expectedMovie = new Movie("id", "title", Duration.ofMinutes(120), "plot", "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg","8.2");
        assertThat(actualMovie, is(equalTo(expectedMovie)));
    }

    @Test
    public void should_get_all_movies_from_service() throws IOException, FormatException {
        setUpMockServer(MOCK_SERVER_MULTIPLE_RESPONSES);
        final var testAppConfig = mock(AppConfig.class);
        when(testAppConfig.getMovieServiceHost()).thenReturn(String.valueOf(mockWebServer.url("/")));
        final var expectedMovie = new Movie("id1", "title", Duration.ofMinutes(120), "plot", "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg","8.2");
        final var defaultMovieGateway = new DefaultMovieGateway(testAppConfig, new OkHttpClient(), new Request.Builder(),
                new ObjectMapper());

        final var actualMovie = defaultMovieGateway.getAllMovies();

        assertThat(actualMovie.get(0), is(equalTo(expectedMovie)));
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}
