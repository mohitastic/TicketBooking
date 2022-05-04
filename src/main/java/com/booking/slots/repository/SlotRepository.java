package com.booking.slots.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Slot findById(int id);
}
