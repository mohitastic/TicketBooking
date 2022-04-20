package com.booking.slots.repository;

import com.booking.movieGateway.MovieGateway;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class SlotService {
    SlotRepository slotRepository;
    ShowService showService;
    MovieGateway movieGateway;

    @Autowired
    public SlotService(SlotRepository slotRepository, ShowService showService, MovieGateway movieGateway) {
        this.showService = showService;
        this.slotRepository = slotRepository;
        this.movieGateway = movieGateway;
    }

    public List<Slot> getByAvailability(Date date) {
        List<Slot> availableSlots = slotRepository.findAll();
        List<Show> shows = showService.fetchAll(date);

        for (Show show : shows) {
            availableSlots.remove(show.getSlot());
        }

        return availableSlots;
    }
}
