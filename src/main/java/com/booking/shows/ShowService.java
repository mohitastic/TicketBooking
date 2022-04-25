package com.booking.shows;

import com.booking.exceptions.ShowException;
import com.booking.movieGateway.MovieGateway;
import com.booking.movieGateway.exceptions.FormatException;
import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.shows.view.models.ShowRequest;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieGateway movieGateway;
    private final SlotService slotService;

    @Autowired
    public ShowService(ShowRepository showRepository, MovieGateway movieGateway, @Lazy SlotService slotService) {
        this.showRepository = showRepository;
        this.movieGateway = movieGateway;
        this.slotService = slotService;
    }

    public List<Show> fetchAll(Date date) {
        return showRepository.findByDate(date);
    }

    public Movie getMovieById(String movieId) throws IOException, FormatException {
        return movieGateway.getMovieFromId(movieId);
    }

    public void addShow(ShowRequest request) throws ShowException, IOException {
        dateIsNotPastDate(request.getDate());

        movieIdExist(request.getMovieId());

        Slot slot = slotService.getSlotById(request.getSlotId());
        slotAlreadyExist(slot);

        notPastSlotOfCurrentDate(request.getDate(), slot);

        //showNotExistForTheGivenSlot(request.getDate(), slot);
        Show bySlotIdAndDate = showRepository.findBySlotIdAndDate(slot.getId(), request.getDate());
        if (bySlotIdAndDate != null)
            throw new ShowException("Show already added in this slot");

        Show show = new Show(request.getDate(), slot, request.getCost(), request.getMovieId());
        showRepository.save(show);
    }

    private void dateIsNotPastDate(Date date) throws ShowException {
        if (isPastDate(date))
            throw new ShowException("Past date not allowed");
    }

    private void movieIdExist(String movieId) throws ShowException, IOException {
        try {
            movieGateway.getMovieFromId(movieId);
        } catch (FormatException e) {
            throw new ShowException("Movie Id does not exist");
        }
    }

    private void slotAlreadyExist(Slot slot) throws ShowException {
        if (slot == null) {
            throw new ShowException("Slot Id does not exist");
        }
    }

    private void notPastSlotOfCurrentDate(Date date, Slot slot) throws ShowException {
        if (isCurrentDate(date) && !futureSlots(slot))
            throw new ShowException("Past slot not allowed");
    }

    private void showNotExistForTheGivenSlot(Date date, Slot slot) throws ShowException {
        if (showRepository.findBySlotIdAndDate(slot.getId(), date) != null)
            throw new ShowException("Show already added in this slot");
    }

    private boolean futureSlots(Slot slot) {
        return slot.getStartTime().compareTo(Time.valueOf(LocalTime.now())) >= 0;
    }

    private boolean isPastDate(Date date) {
        return date.compareTo(Date.valueOf(LocalDate.now())) < 0;
    }

    private boolean isCurrentDate(Date date) {
        return date.compareTo(Date.valueOf(LocalDate.now())) == 0;
    }
}
