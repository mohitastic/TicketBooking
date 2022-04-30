package com.booking.users.repository;

import com.booking.users.repository.model.UserDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository  extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByPhoneNumber(String phoneNumber);
    //@EntityGraph(value = "UserDetail.user", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserDetail> findByUserId(Long id);
}
