package com.booking.slots.repository.view.model;

import com.booking.movieGateway.models.Movie;
import com.booking.shows.respository.Show;
import com.booking.slots.repository.Slot;
import com.booking.utilities.serializers.date.DateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@ApiModel("Slot Response")
public class SlotResponse {

    @JsonProperty
    @ApiModelProperty(required = true, position = 5)
    private final List<Slot> slots;

    @JsonProperty
    @ApiModelProperty(required = true, position = 5)
    private final List<Movie> movies;

    public SlotResponse(List<Movie> movies, List<Slot> slots) {
        this.movies = movies;
        this.slots = slots;
    }
}
