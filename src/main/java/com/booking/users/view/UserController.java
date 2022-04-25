package com.booking.users.view;

import com.booking.exceptions.ChangePasswordException;
import com.booking.exceptions.UserSignUpException;
import com.booking.handlers.models.ErrorResponse;
import com.booking.users.ChangePasswordService;
import com.booking.users.UserSignUpService;
import com.booking.users.view.model.ChangePasswordRequest;
import com.booking.users.view.model.UserSignUpRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {
    private final ChangePasswordService changePasswordService;
    private final UserSignUpService userSignUpService;

    @Autowired
    public UserController(ChangePasswordService changePasswordService, UserSignUpService userSignUpService) {
        this.changePasswordService = changePasswordService;
        this.userSignUpService = userSignUpService;
    }

    @GetMapping("/login")
    Map<String, Object> login(Principal principal) {
        String username = principal.getName();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);
        return userDetails;
    }

    @PutMapping("/changePassword")
    void updatePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ChangePasswordException {
        System.out.println("change password request "+ changePasswordRequest.getOldPassword());
        changePasswordService.execute(changePasswordRequest, principal.getName());
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
        userSignUpService.execute(userSignUpRequest);
    }
}
