package com.booking.users;

import com.booking.exceptions.*;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.view.model.UserSignUpRequest;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserSignUpService {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";
    private static final String PHONE_NUMBER_PATTERN = "\\d{10}";
    private static final String EMAIL_ADDRESS_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private UserRepository userRepository;
    public UserSignUpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserSignUpRequest userSignUpRequest) throws UserSignUpException {
        String name = userSignUpRequest.getName();
        String username = userSignUpRequest.getUsername();
        String email = userSignUpRequest.getEmail();
        String phoneNumber = userSignUpRequest.getPhoneNumber();
        String password = userSignUpRequest.getPassword();
        String confirmPassword = userSignUpRequest.getConfirmPassword();

        fieldNotEmptyCheck(name, username, email, phoneNumber, password, confirmPassword);

        passwordSameAsConfirmPassword(password, confirmPassword);

        patternMatchCheckForPassword(password);

        patternMatchCheckForPhoneNumber(phoneNumber);

        patternMatchCheckForEmailAddress(email);

        Optional<User> user = userRepository.findByUsername(username);
        userAlreadyExists(user);
    }


    private void userAlreadyExists(Optional<User> user) throws UserSignUpException{
        if (! user.isEmpty()){
            throw new UserSignUpException("User name already exists");
        }
    }

    private void patternMatchCheckForEmailAddress(String email) throws UserSignUpException {
        if(!Pattern.matches(EMAIL_ADDRESS_PATTERN, email)){
            throw new UserSignUpException("Email address is not valid");
        }
    }

    private void patternMatchCheckForPhoneNumber(String phoneNumber) throws UserSignUpException {
        if(!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)){
            throw new UserSignUpException("Phone number is not valid");
        }
    }

    private void patternMatchCheckForPassword(String password) throws UserSignUpException {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new UserSignUpException("Password does not match the pattern");
        }
    }

    private void passwordSameAsConfirmPassword(String password, String confirmPassword) throws UserSignUpException {
        if(!password.equals(confirmPassword)) {
            throw new UserSignUpException("Password does not match Confirm Password");
        }
    }

    private void fieldNotEmptyCheck(String name, String username, String email, String phoneNumber, String password, String confirmPassword) throws UserSignUpException {
        if(name.equals("") || username.equals("") || email.equals("") || phoneNumber.equals("") || password.equals("") || confirmPassword.equals("") ){
            throw new UserSignUpException("The fields cannot be empty.");
        }
    }
}