package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.customers.repository.Customer;
import com.booking.customers.repository.CustomerRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.exceptions.PatternDoesNotMatchException;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.slots.repository.Slot;
import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;
import static com.booking.users.Role.Code.CUSTOMER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceTest {
    public static final Long TEST_SHOW_ID = 1L;
    private BookingRepository bookingRepository;
    private BookingService bookingService;
    private Date bookingDate;
    private Show show;
    private User user;
    private Customer customer;
    private CustomerRepository customerRepository;
    private ShowRepository showRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        bookingRepository = mock(BookingRepository.class);
        customerRepository = mock(CustomerRepository.class);
        showRepository = mock(ShowRepository.class);
        userRepository = mock(UserRepository.class);
        bookingDate = Date.valueOf("2020-06-01");
        Slot slot = new Slot("13:00-16:00", Time.valueOf("13:00:00"), Time.valueOf("16:00:00"));
        show = new Show(bookingDate, slot, BigDecimal.valueOf(250), "1");
        customer = new Customer("Customer name", "9090909090");
        user = new User(1L,"testUser","foobar", CUSTOMER);
        bookingService = new BookingService(bookingRepository, customerRepository,userRepository, showRepository);
    }

    @Test
    public void should_save_booking_for_walkIn_customer() throws NoSeatAvailableException, PatternDoesNotMatchException {
        int noOfSeats = 2;
        Booking booking = new Booking(bookingDate, show, customer, noOfSeats, BigDecimal.valueOf(500));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        Booking mockBooking = mock(Booking.class);
        when(bookingRepository.save(booking)).thenReturn(mockBooking);

        Booking actualBooking = bookingService.bookWalkInCustomer(customer, TEST_SHOW_ID, bookingDate, noOfSeats);

        verify(bookingRepository).save(booking);
        assertThat(actualBooking, is(equalTo(mockBooking)));
    }

    @Test
    public void should_save_booking_for_user_customer() throws NoSeatAvailableException, PatternDoesNotMatchException {
        int noOfSeats = 2;
        Booking booking = new Booking(bookingDate, show, user, noOfSeats, BigDecimal.valueOf(500));
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Booking mockBooking = mock(Booking.class);
        when(bookingRepository.save(booking)).thenReturn(mockBooking);

        Booking actualBooking = bookingService.bookUserCustomer(user.getUsername(), TEST_SHOW_ID, bookingDate, noOfSeats);

        verify(bookingRepository).save(booking);
        assertThat(actualBooking, is(equalTo(mockBooking)));

    }

    @Test
    public void should_save_walkIn_customer_who_requests_booking() throws NoSeatAvailableException, PatternDoesNotMatchException {
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        bookingService.bookWalkInCustomer(customer, TEST_SHOW_ID, bookingDate, 2);

        verify(customerRepository).save(customer);
    }

    @Test
    public void should_not_book_seat_when_seats_are_not_available() {
        when(bookingRepository.bookedSeatsByShow(show.getId())).thenReturn(TOTAL_NO_OF_SEATS);
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.of(show));
        assertThrows(NoSeatAvailableException.class, () -> bookingService.bookWalkInCustomer(customer, TEST_SHOW_ID, bookingDate, 2));
        verifyNoInteractions(customerRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void should_not_book_seat_when_show_is_not_found() {
        when(showRepository.findById(TEST_SHOW_ID)).thenReturn(Optional.empty());
        final var emptyResultDataAccessException =
                assertThrows(EmptyResultDataAccessException.class,
                        () -> bookingService.bookWalkInCustomer(customer, TEST_SHOW_ID, bookingDate, 2));

        assertThat(emptyResultDataAccessException.getMessage(), is(equalTo("Show not found")));
        assertThat(emptyResultDataAccessException.getExpectedSize(), is(equalTo(1)));
        verifyNoInteractions(customerRepository);
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
