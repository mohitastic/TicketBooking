package com.booking.slot;

import com.booking.exceptions.SlotException;
import com.booking.shows.ShowService;
import com.booking.shows.respository.Show;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import com.booking.slots.repository.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SlotServiceTest {
    private SlotRepository mockSlotRepository;

    @BeforeEach
    public void beforeEach() {
        mockSlotRepository = mock(SlotRepository.class);
    }

    @Test
    void shouldThrowExceptionWhenPastDateGiven() {
        ShowService showService= Mockito.mock(ShowService.class);
        SlotService slotService = new SlotService(mockSlotRepository,showService);
        String expectedExceptionMessage = "Past dates not allowed";

        SlotException slotException = assertThrows(SlotException.class, () -> slotService.getByAvailability(Date.valueOf("2022-04-21")));

        assertEquals(slotException.getMessage(),expectedExceptionMessage);
    }

    @Test
    void shouldReturnAllAvailableSlotsForGivenDate() throws SlotException {
        ShowService showService = Mockito.mock(ShowService.class);
        SlotService slotService = new SlotService(mockSlotRepository, showService);
        Date date = Date.valueOf(LocalDate.now());
        Slot slotOne = new Slot("slot1", Time.valueOf("09:00:00"), Time.valueOf("12:30:00"));
        Slot slotTwo = new Slot("slot2", Time.valueOf("13:30:00"), Time.valueOf("17:30:00"));
        Slot slotThree = new Slot("slot3", Time.valueOf(LocalTime.now().plusHours(2)), Time.valueOf(LocalTime.now().plusHours(5)));
        List<Slot> slots = new ArrayList<>();
        slots.add(slotOne);
        slots.add(slotTwo);
        slots.add(slotThree);
        List<Show> shows = new ArrayList<>();
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
        List<Slot> expectedSlots = new ArrayList<>();
        expectedSlots.add(slotThree);
        when(showService.fetchAll(date)).thenReturn(shows);
        when(mockSlotRepository.findAll()).thenReturn(slots);

        List<Slot> availableSlots = slotService.getByAvailability(date);

        assertThat(availableSlots, is(equalTo(expectedSlots)));
    }
}