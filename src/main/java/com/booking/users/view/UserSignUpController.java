package com.booking.users.view;

import com.booking.exceptions.UserSignUpException;
import com.booking.handlers.models.ErrorResponse;
import com.booking.users.UserSignUpService;
import com.booking.users.view.model.UserSignUpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "User Details")
@RestController
public class UserSignUpController {
    private final UserSignUpService userSignUpService;

    @Autowired
    public UserSignUpController(UserSignUpService userSignUpService) {
        this.userSignUpService = userSignUpService;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Create a new user")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User signed up successfully"),
            @ApiResponse(code = 400, message = "Server cannot process request due to client error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Something failed in the server", response = ErrorResponse.class)
    })
    void userSignUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) throws UserSignUpException {
        System.out.println("Request "+userSignUpRequest.getName());
        userSignUpService.execute(userSignUpRequest);
    }
}
