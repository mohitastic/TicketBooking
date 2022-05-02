package com.booking.slots.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Slot findById(int id);
}
