package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.customers.repository.Customer;
import com.booking.customers.repository.CustomerRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.regex.Pattern;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;

@Service
public class BookingService {
    private static  final String NAME_PATTERN = "^(?![\\s.]+$)[a-zA-Z\\s.]*$";
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ShowRepository showRepository;

    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, ShowRepository showRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
    }

    public Booking book(Customer customer, Long showId, Date bookingDate, int noOfSeats) throws NoSeatAvailableException, PatternDoesNotMatchException {
        final var show = showRepository.findById(showId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Show not found", 1));

        if (availableSeats(show) < noOfSeats) {
            throw new NoSeatAvailableException("No seats available");
        }

        patternMatchCheckForName(customer.getName());
        customerRepository.save(customer);
        BigDecimal amountPaid = show.costFor(noOfSeats);
        return bookingRepository.save(new Booking(bookingDate, show, customer, noOfSeats, amountPaid));
    }

    private void patternMatchCheckForName(String name) throws PatternDoesNotMatchException {
        if(!Pattern.matches(NAME_PATTERN, name)){
            throw new PatternDoesNotMatchException("Name does not match the pattern");
        }
    }

    private long availableSeats(Show show) {
        Integer bookedSeats = bookingRepository.bookedSeatsByShow(show.getId());
        if (noSeatsBooked(bookedSeats))
            return TOTAL_NO_OF_SEATS;

        return TOTAL_NO_OF_SEATS - bookedSeats;
    }

    private boolean noSeatsBooked(Integer bookedSeats) {
        return bookedSeats == null;
    }
}
