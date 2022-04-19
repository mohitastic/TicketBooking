package com.booking.movieGateway;

import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public interface MovieGateway {

    Movie getMovieFromId(String id) throws IOException, FormatException;

    List<Movie> getAllMovies() throws IOException;
}
