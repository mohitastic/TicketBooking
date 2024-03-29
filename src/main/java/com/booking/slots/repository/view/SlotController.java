package com.booking.slots.repository.view;

import com.booking.exceptions.SlotException;
import com.booking.handlers.models.ErrorResponse;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import com.booking.slots.repository.view.model.SlotResponse;
import com.booking.toggles.FeatureAssociation;
import com.booking.toggles.Features;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

import static com.booking.users.Role.Code.ADMIN;

@Api(tags = "Slots")
@RestController
@Secured("ROLE_" + ADMIN)
@RequestMapping("/slots")
public class SlotController {

    private final SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @FeatureAssociation(value = Features.MOVIE_SCHEDULE)
    @GetMapping
    @ApiOperation(value = "Fetch available slots for given date")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched available slots successfully"),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public SlotResponse availableSlots(@Valid @RequestParam(name = "date") Date date) throws SlotException {
        List<Slot> availableSlots = slotService.getByAvailability(date);
        return new SlotResponse(availableSlots);
    }
}
