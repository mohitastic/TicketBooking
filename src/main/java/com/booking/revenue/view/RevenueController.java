package com.booking.revenue.view;

import com.booking.handlers.models.ErrorResponse;
import com.booking.revenue.RevenueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Date;

import static com.booking.users.Role.Code.ADMIN;

@Api(tags = "Revenue")
@RestController
@Secured("ROLE_" + ADMIN)
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    @ApiOperation(value = "Fetch revenue")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Fetched revenue successfully"),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    public BigDecimal getRevenue(@Valid @RequestParam(name = "date") Date date) {
        return revenueService.revenueByDate(date);
    }
}
