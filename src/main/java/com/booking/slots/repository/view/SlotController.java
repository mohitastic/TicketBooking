package com.booking.slots.repository.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.shows.view.models.ShowResponse;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import com.booking.slots.repository.view.model.SlotResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "Slots")
@RestController
@RequestMapping("/slots")
public class SlotController {

    private final SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    @ApiOperation(value = "Fetch available slots with movies")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched available slots successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public SlotResponse availableSlots(@Valid @RequestParam(name = "date") Date date) throws IOException, FormatException {
        List<Slot> availableSlots = slotService.getByAvailability(date);
        List<Movie> movies = slotService.getAllMovies();

        return new SlotResponse(movies,availableSlots);
    }

}
