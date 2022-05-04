package com.booking.bookings.view;

import com.booking.customers.repository.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMax;
import java.sql.Date;

import static com.booking.shows.respository.Constants.MAX_NO_OF_SEATS_PER_BOOKING;

public class BookingRequest {

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "date", value = "Date of booking (yyyy-MM-dd)", dataType = "java.lang.String", required = true, example = "2020-01-01")
    private Date date;

    @JsonProperty
    @ApiModelProperty(name = "showId", value = "The show id", required = true)
    private Long showId;

    @JsonProperty
    @ApiModelProperty(name = "customer", value = "WalkIn Customer requesting booking")
    private Customer customer;

    @JsonProperty
    @ApiModelProperty(name = "username", value = "User customer requesting booking")
    private String username;

    @JsonProperty
    @DecimalMax(value = MAX_NO_OF_SEATS_PER_BOOKING, message = "Maximum {value} seats allowed per booking")
    @ApiModelProperty(name = "no of seats", value = "Seats requested to be booked", example = "3", required = true)
    private int noOfSeats;

    public Date getDate() {
        return date;
    }

    public Long getShowId() {
        return showId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public String getUsername() {
        return username;
    }

    public BookingRequest() {
    }

    public BookingRequest(Date date, Long showId, Customer customer, int noOfSeats) {
        this.date = date;
        this.showId = showId;
        this.customer = customer;
        this.noOfSeats = noOfSeats;
    }

    public BookingRequest(Date date, Long showId, String username, int noOfSeats) {
        this.date = date;
        this.showId = showId;
        this.username = username;
        this.noOfSeats = noOfSeats;
    }
}
