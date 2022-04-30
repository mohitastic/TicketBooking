package com.booking.users;

import com.booking.exceptions.UserDetailNotFoundException;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService {
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    public UserDetail fetch(String username) throws UserDetailNotFoundException {
        UserDetail userDetail = null;
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isEmpty()){
            User currentUser = user.get();
            Optional<UserDetail> userDetails = userDetailsRepository.findByUserId(currentUser.getId());
            if(!userDetails.isEmpty()){
                userDetail = userDetails.get();
            }
            else{
                throw new UserDetailNotFoundException();
            }
        }
        return userDetail;
    }
}
