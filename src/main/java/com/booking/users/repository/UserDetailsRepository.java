package com.booking.users.repository;

import com.booking.users.repository.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository  extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByPhoneNumber(String phoneNumber);
}
