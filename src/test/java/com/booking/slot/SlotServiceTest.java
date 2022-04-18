package com.booking.slot;

import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import com.booking.slots.repository.SlotService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

class SlotServiceTest {

    @Test
    void shouldNotReturnTheSlotsWhichAreAlreadyBooked() {
        ShowService showService = Mockito.mock(ShowService.class);
        SlotRepository slotRepository = Mockito.mock(SlotRepository.class);
        SlotService slotService = new SlotService(slotRepository, showService);
        Date date = Date.valueOf("2022-04-16");
        List<Show> shows = new ArrayList<>();
        Slot slotOne = new Slot("slot1", Time.valueOf("09:00:00"), Time.valueOf("12:30:00"));
        Slot slotTwo = new Slot("slot2", Time.valueOf("13:30:00"), Time.valueOf("17:30:00"));
        Slot slotThree = new Slot("slot3", Time.valueOf("18:00:00"), Time.valueOf("21:30:00"));
        shows.add(new Show(
                date,
                slotOne,
                new BigDecimal("299.99"),
                "movie_1"));

        shows.add(new Show(
                date,
                slotTwo,
                new BigDecimal("299.99"),
                "movie_2"));
        List<Slot> slots = new ArrayList<>();
        slots.add(slotOne);
        slots.add(slotTwo);
        slots.add(slotThree);
        List<Slot> expectedSlots = new ArrayList<>();
        expectedSlots.add(slotThree);
        when(showService.fetchAll(date)).thenReturn(shows);
        when(slotRepository.findAll()).thenReturn(slots);

        List<Slot> availableSlots = slotService.getByAvailability(date);

        assertThat(availableSlots, is(equalTo(expectedSlots)));
    }
}