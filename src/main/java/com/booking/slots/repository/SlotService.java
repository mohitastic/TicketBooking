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

    public List<Slot> getByAvailability(Date date) throws SlotException {

        if (date.compareTo(Date.valueOf(LocalDate.now())) < 0) {
            throw new SlotException("Past dates not allowed");
        }

        List<Slot> availableSlots = slotRepository.findAll();
        List<Show> shows = showService.fetchAll(date);

        for (Show show : shows) {
            availableSlots.remove(show.getSlot());
        }

        return availableSlots.stream().filter(slot -> slot.getStartTime().compareTo(Time.valueOf(LocalTime.now())) >= 0).collect(Collectors.toList());
    }
}
