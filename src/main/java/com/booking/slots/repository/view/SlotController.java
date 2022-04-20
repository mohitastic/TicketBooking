package com.booking.slots.repository.view;

import com.booking.handlers.models.ErrorResponse;
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
import java.sql.Date;
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
    @ApiOperation(value = "Fetch available slots for given date")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched available slots successfully"),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public SlotResponse availableSlots(@Valid @RequestParam(name = "date") Date date) {
        List<Slot> availableSlots = slotService.getByAvailability(date);
        return new SlotResponse(availableSlots);
    }
}
