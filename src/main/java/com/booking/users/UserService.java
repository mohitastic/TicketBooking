package com.booking.users;

import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String fetchRole(String username) throws UsernameNotFoundException {
        String userRole;
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User currentUser = user.get();
            userRole = currentUser.getRole();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
        return userRole;
    }
}
