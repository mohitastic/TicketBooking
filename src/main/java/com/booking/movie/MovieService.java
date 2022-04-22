package com.booking.movie;

import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService {
    private final MovieGateway movieGateway;

    @Autowired
    public MovieService(MovieGateway movieGateway) {
        this.movieGateway = movieGateway;
    }

    public List<Movie> getAllMovies() throws IOException {
        return movieGateway.getAllMovies();
    }
}
