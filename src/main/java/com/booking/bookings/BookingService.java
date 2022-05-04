package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.customers.repository.Customer;
import com.booking.customers.repository.CustomerRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, UserRepository userRepository,ShowRepository showRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
    }

    public Booking bookWalkInCustomer(Customer customer, Long showId, Date bookingDate, int noOfSeats) throws NoSeatAvailableException, PatternDoesNotMatchException {
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

    public Booking bookUserCustomer(String username, Long showId, Date bookingDate, int noOfSeats) throws NoSeatAvailableException, UsernameNotFoundException {
        final var show = showRepository.findById(showId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Show not found", 1));

        if (availableSeats(show) < noOfSeats) {
            throw new NoSeatAvailableException("No seats available");
        }

        User user;
        if(userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }else {
            user = userRepository.findByUsername(username).get();
        }


        BigDecimal amountPaid = show.costFor(noOfSeats);
        return bookingRepository.save(new Booking(bookingDate, show, user, noOfSeats, amountPaid));
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
