package com.booking.users.view;

import com.booking.exceptions.ChangePasswordException;
import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.exceptions.UserDetailNotFoundException;
import com.booking.exceptions.UserSignUpException;
import com.booking.handlers.models.ErrorResponse;
import com.booking.toggles.FeatureAssociation;
import com.booking.toggles.Features;
import com.booking.users.ChangePasswordService;
import com.booking.users.UserDetailService;
import com.booking.users.UserService;
import com.booking.users.UserSignUpService;
import com.booking.users.repository.model.UserDetail;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {
    private final ChangePasswordService changePasswordService;
    private final UserSignUpService userSignUpService;
    private final UserDetailService userDetailsService;
    private final UserService userService;

    @Autowired
    public UserController(ChangePasswordService changePasswordService, UserSignUpService userSignUpService, UserDetailService userDetailsService, UserService userService) {
        this.changePasswordService = changePasswordService;
        this.userSignUpService = userSignUpService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @GetMapping("/login")
    Map<String, Object> login(Principal principal) {
        String username = principal.getName();
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", username);
        String userRole = userService.fetchRole(username);
        userDetails.put("role", userRole);
        return userDetails;
    }

    @FeatureAssociation(value = Features.CHANGE_PASSWORD)
    @PutMapping("/changePassword")
    void updatePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ChangePasswordException {
        System.out.println("change password request " + changePasswordRequest.getOldPassword());
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
    void userSignUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) throws UserSignUpException, PatternDoesNotMatchException {
        userSignUpService.execute(userSignUpRequest);
    }

    @FeatureAssociation(value = Features.VIEW_USER_PROFILE)
    @GetMapping("/userDetails")
    Map<String, Object> userDetails(@Valid @RequestParam(name = "username") String username) throws UserDetailNotFoundException {
        UserDetail userDetails = userDetailsService.fetch(username);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(userDetails.getDob());
        Map<String, Object> user = new HashMap<>();
        user.put("name", userDetails.getName());
        user.put("username", userDetails.getUser().getUsername());
        user.put("dob", date);
        user.put("email", userDetails.getEmail());
        user.put("phoneNumber", userDetails.getPhoneNumber());

        return user;
    }
}
