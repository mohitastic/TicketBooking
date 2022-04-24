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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieGateway movieGateway;
    private final SlotService slotService;

    @Autowired
    public ShowService(ShowRepository showRepository, MovieGateway movieGateway, SlotService slotService) {
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

    public void addShow(ShowRequest request) throws ShowException, IOException, FormatException {
        if (request.getDate().compareTo(Date.valueOf(LocalDate.now())) < 0)
            throw new ShowException("400 : Past date not allowed");

        if (movieGateway.getMovieFromId(request.getMovieId()) == null)
            throw new ShowException("404 : Movie Id does not exist");

        Slot slot = slotService.getSlotById(request.getSlotId());

        if(slot == null){
            throw new ShowException("404 : Slot Id does not exist");
        }

        Show show = new Show(request.getDate(), slot, request.getCost(), request.getMovieId());
        showRepository.save(show);
    }
}
