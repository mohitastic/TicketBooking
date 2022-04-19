package com.booking.users.view;

import com.booking.exceptions.ChangePasswordException;
import com.booking.users.ChangePasswordService;
import com.booking.users.view.model.ChangePasswordRequest;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Users")
@RestController
public class UserController {
    private final ChangePasswordService changePasswordService;

    public UserController(ChangePasswordService changePasswordService) {
        this.changePasswordService = changePasswordService;
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
}
