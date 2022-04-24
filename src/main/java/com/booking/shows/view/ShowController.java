package com.booking.shows.view;

import com.booking.exceptions.ShowException;
import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.shows.view.models.ShowRequest;
import com.booking.shows.view.models.ShowResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Shows")
@RestController
@RequestMapping("/shows")
public class ShowController {
    private final ShowService showService;

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    @ApiOperation(value = "Fetch shows")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched shows successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public List<ShowResponse> fetchAll(@Valid @RequestParam(name = "date") Date date) throws IOException, FormatException {
        List<ShowResponse> showResponse = new ArrayList<>();
        for (Show show : showService.fetchAll(date)) {
            showResponse.add(constructShowResponse(show));
        }
        return showResponse;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "create new show")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a booking successfully"),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public void add(@RequestBody ShowRequest showRequest) throws ShowException {
        showService.addShow(showRequest);
    }

    private ShowResponse constructShowResponse(Show show) throws IOException, FormatException {
        Movie movie = showService.getMovieById(show.getMovieId());
        return new ShowResponse(movie, show.getSlot(), show);
    }
}
