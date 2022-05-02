package com.booking.users;

import com.booking.exceptions.*;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import com.booking.users.view.model.UserSignUpRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.booking.users.Role.Code.CUSTOMER;

@Service
public class UserSignUpService {
    private static  final String NAME_PATTERN = "^(?![\\s.]+$)[a-zA-Z\\s.]*$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,64}$";
    private static final String PHONE_NUMBER_PATTERN = "^[1-9]([0-9]{9})$";
    private static final String EMAIL_ADDRESS_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private UserRepository userRepository;
    private UserDetailsRepository userDetailsRepository;
    public UserSignUpService(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }
    private static final Date TODAY_DATE  = Date.valueOf(String.valueOf(java.time.LocalDate.now()));

    public void execute(UserSignUpRequest userSignUpRequest) throws UserSignUpException, PatternDoesNotMatchException {
        String name = userSignUpRequest.getName();
        String username = userSignUpRequest.getUsername();
        String email = userSignUpRequest.getEmail();
        String phoneNumber = userSignUpRequest.getPhoneNumber();
        String password = userSignUpRequest.getPassword();
        String confirmPassword = userSignUpRequest.getConfirmPassword();
        Date dob = userSignUpRequest.getDob();

        fieldNotEmptyCheck(name, username, email, phoneNumber, password, confirmPassword);

        patternMatchCheckForName(name);

        validateForFutureDate(dob);

        passwordSameAsConfirmPassword(password, confirmPassword);

        patternMatchCheckForPassword(password);

        patternMatchCheckForPhoneNumber(phoneNumber);

        patternMatchCheckForEmailAddress(email);

        Optional<User> user = userRepository.findByUsername(username);
        userAlreadyExists(user);
        Optional<UserDetail> userDetails = userDetailsRepository.findByPhoneNumber(phoneNumber);
        phoneNumberAlreadyExists(userDetails);
        User savedUser = userRepository.save(new User(username, password, CUSTOMER));
        userDetailsRepository.save(new UserDetail(name, dob, email, phoneNumber, savedUser));
    }

    private void validateForFutureDate(Date dob) throws UserSignUpException {
        if(dob.after(TODAY_DATE)){
            throw new UserSignUpException("Invalid Date Of Birth");
        }
    }

    private void phoneNumberAlreadyExists(Optional<UserDetail> userDetails) throws UserSignUpException {
        if(!userDetails.isEmpty()){
            throw new UserSignUpException("Phone Number already exists");
        }
    }


    private void userAlreadyExists(Optional<User> user) throws UserSignUpException{
        if (! user.isEmpty()){
            throw new UserSignUpException("User name already exists");
        }
    }

    private void patternMatchCheckForName(String name) throws PatternDoesNotMatchException {
        if(!Pattern.matches(NAME_PATTERN, name)){
            throw new PatternDoesNotMatchException("Name does not match the pattern");
        }
    }

    private void patternMatchCheckForEmailAddress(String email) throws PatternDoesNotMatchException {
        if(!Pattern.matches(EMAIL_ADDRESS_PATTERN, email)){
            throw new PatternDoesNotMatchException("Email address is not valid");
        }
    }

    private void patternMatchCheckForPhoneNumber(String phoneNumber) throws PatternDoesNotMatchException {
        if(!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)){
            throw new PatternDoesNotMatchException("Phone number is not valid");
        }
    }

    private void patternMatchCheckForPassword(String password) throws PatternDoesNotMatchException {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new PatternDoesNotMatchException("Password does not match the pattern");
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
