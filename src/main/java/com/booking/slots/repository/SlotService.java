package com.booking.slots.repository;

import com.booking.exceptions.SlotException;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotService {
    SlotRepository slotRepository;
    ShowService showService;

    @Autowired
    public SlotService(SlotRepository slotRepository, ShowService showService) {
        this.showService = showService;
        this.slotRepository = slotRepository;
    }

    public Slot getSlotById(int slotId) {
        return slotRepository.getById(slotId);
    }

    public Slot findById(int slotId) {
        return slotRepository.findById(slotId);
    }

    public List<Slot> getByAvailability(Date date) throws SlotException {
        if (isPastDate(date)) {
            throw new SlotException("Past date not allowed");
        }

        List<Slot> slots = slotRepository.findAll();
        List<Show> shows = showService.fetchAll(date);

        for (Show show : shows) {
            slots.remove(show.getSlot());
        }

        return isFutureDate(date) ? slots : slots
                .stream()
                .filter(this::futureSlots)
                .collect(Collectors.toList());
    }

    private boolean futureSlots(Slot slot) {
        return slot.getStartTime().compareTo(Time.valueOf(LocalTime.now())) >= 0;
    }

    private boolean isPastDate(Date date) {
        return date.compareTo(Date.valueOf(LocalDate.now())) < 0;
    }

    private boolean isFutureDate(Date date) {
        return date.compareTo(Date.valueOf(LocalDate.now())) > 0;
    }
}
