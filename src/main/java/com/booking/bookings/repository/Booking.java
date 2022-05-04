package com.booking.bookings.repository;

import com.booking.bookings.view.BookingConfirmationResponse;
import com.booking.customers.repository.Customer;
import com.booking.shows.respository.Show;
import com.booking.users.repository.UserDetailsRepository;
import com.booking.users.repository.model.User;
import com.booking.users.repository.model.UserDetail;
import com.booking.utilities.serializers.date.DateSerializer;
import com.booking.users.UserDetailService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @ApiModelProperty(name = "id", value = "The booking id", example = "0", position = 1)
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @NotNull(message = "Date must be provided")
    @ApiModelProperty(name = "date", value = "Date of booking (yyyy-MM-dd)", dataType = "java.lang.String", required = true, example = "2020-01-01", position = 2)
    private Date date;

    @OneToOne
    @JoinColumn(name = "show_id")
    @JsonIgnore
    @NotNull(message = "Show must be provided")
    private Show show;

    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @Column(name = "no_of_seats", nullable = false)
    @JsonProperty
    @DecimalMin(value = "0", inclusive = false, message = "Seats should be greater than {value}")
    @NotNull(message = "No of seats must be provided")
    @ApiModelProperty(name = "no of seats", value = "Number of seats/tickets for show", required = true, example = "3", position = 3)
    private Integer noOfSeats;

    @Column(name = "amount_paid", nullable = false)
    @JsonProperty
    @DecimalMin(value = "0", inclusive = false, message = "Amount paid should be greater than {value}")
    @NotNull(message = "Amount paid must be provided")
    @ApiModelProperty(name = "amount paid", value = "Amount paid for seats", required = true, example = "300.44", position = 4)
    private BigDecimal amountPaid;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Booking(Date date, Show show, Customer customer, Integer noOfSeats, BigDecimal amountPaid) {
        this.date = date;
        this.show = show;
        this.customer = customer;
        this.noOfSeats = noOfSeats;
        this.amountPaid = amountPaid;
    }



    public Booking() {
    }

    public Booking(Date date, Show show, User user, int noOfSeats, BigDecimal amountPaid) {
        this.date = date;
        this.show = show;
        this.user = user;
        this.noOfSeats = noOfSeats;
        this.amountPaid = amountPaid;
        System.out.println("Inside BOOKING ");
    }



    public BookingConfirmationResponse constructBookingConfirmation() {
        if(customer!=null) {
            return new BookingConfirmationResponse(
                    id,
                    customer.getName(),
                    show.getDate(),
                    show.getSlot().getStartTime(),
                    amountPaid,
                    noOfSeats
            );
        }

        return new BookingConfirmationResponse(
                id,
                user.getUsername(),
                show.getDate(),
                show.getSlot().getStartTime(),
                amountPaid,
                noOfSeats);

    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public Date getDate() {
        return date;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(date, booking.date) &&
                Objects.equals(show, booking.show) &&
                Objects.equals(customer, booking.customer) &&
                Objects.equals(noOfSeats, booking.noOfSeats) &&
                Objects.equals(amountPaid, booking.amountPaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, show, customer, noOfSeats, amountPaid);
    }
}
