package com.booking.movie.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movie.MovieService;
import com.booking.movieGateway.models.Movie;
import com.booking.toggles.FeatureAssociation;
import com.booking.toggles.Features;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static com.booking.users.Role.Code.ADMIN;

@Api(tags = "Movies")
@RestController
@Secured("ROLE_" + ADMIN)
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @FeatureAssociation(value = Features.MOVIE_SCHEDULE)
    @GetMapping
    @ApiOperation(value = "Fetch all movies")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched movies successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public List<Movie> fetchAll() throws IOException {
        return movieService.getAllMovies();
    }
}
