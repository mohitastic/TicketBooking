package com.booking.shows.view.models;

import com.booking.slots.repository.Slot;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.sql.Date;

public class ShowRequest {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "date", value = "Date of booking (yyyy-MM-dd)", dataType = "java.lang.String", required = true, example = "2020-01-01", position = 1)
    private final Date date;

    @JsonProperty
    @ApiModelProperty(name = "slot", value = "slot", required = true, position = 2)
    private final Slot slot;

    @JsonProperty
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost should be greater than {value}")
    @Digits(integer = 4, fraction = 2, message = "Cost can have at most {integer} integral digits, and {fraction} fractional digits")
    private final BigDecimal cost;

    @JsonProperty
    @ApiModelProperty(name = "movieId", value = "The movie id", dataType = "java.lang.String", required = true, position = 3)
    private final String movieId;

    public Date getDate() {
        return date;
    }

    public Slot getSlot() {
        return slot;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getMovieId() {
        return movieId;
    }

    public ShowRequest(Date date, Slot slot, BigDecimal cost, String movieId) {
        this.date = date;
        this.slot = slot;
        this.cost = cost;
        this.movieId = movieId;
    }
}
