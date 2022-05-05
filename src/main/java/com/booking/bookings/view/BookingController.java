package com.booking.bookings.view;

import com.booking.bookings.BookingService;
import com.booking.bookings.repository.Booking;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.handlers.models.ErrorResponse;
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

import static com.booking.users.Role.Code.ADMIN;
import static com.booking.users.Role.Code.CUSTOMER;

@Api(tags = "Bookings")
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Secured("ROLE_" + ADMIN)
    @PostMapping("/walkInCustomer")
    @ApiOperation(value = "Create a booking for walk in customer")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a booking successfully"),
            @ApiResponse(code = 404, message = "Record not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })

    public BookingConfirmationResponse bookWalkInCustomer(@Valid @RequestBody BookingRequest bookingRequest) throws NoSeatAvailableException, PatternDoesNotMatchException {
        Booking booking = bookingService.bookWalkInCustomer(bookingRequest.getCustomer(), bookingRequest.getShowId(), bookingRequest.getDate(), bookingRequest.getNoOfSeats());
        return booking.constructBookingConfirmation();
    }

    @FeatureAssociation(value= Features.CUSTOMER_BOOKING)
    @Secured("ROLE_" + CUSTOMER)
    @PostMapping("/userCustomer")
    @ApiOperation(value = "Create a booking")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created a booking successfully"),
            @ApiResponse(code = 404, message = "Record not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })

    public BookingConfirmationResponse bookUserCustomer(@Valid @RequestBody BookingRequest bookingRequest) throws NoSeatAvailableException, PatternDoesNotMatchException {
        Booking booking = bookingService.bookUserCustomer(bookingRequest.getUsername(), bookingRequest.getShowId(), bookingRequest.getDate(), bookingRequest.getNoOfSeats());
        return booking.constructBookingConfirmation();
    }
}
