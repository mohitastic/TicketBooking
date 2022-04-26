package com.booking.shows.view.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

public class ShowRequest {

    @NotNull
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "date", value = "Date of booking (yyyy-MM-dd)", dataType = "java.lang.String", required = true, example = "2020-01-01", position = 1)
    private Date date;

    @NotNull
    @JsonProperty
    @ApiModelProperty(name = "slot", value = "slot", required = true, position = 2)
    private int slotId;

    @NotNull
    @JsonProperty
    @DecimalMin(value = "0.0", message = "Cost should be greater than {value}")
    @ApiModelProperty(name = "cost", value = "cost of movie", required = true, position = 3)
    private BigDecimal cost;

    @NotNull
    @JsonProperty
    @ApiModelProperty(name = "movie id", value = "The movie id", required = true, position = 4)
    private String movieId;

    public Date getDate() {
        return date;
    }

    public int getSlotId() {
        return slotId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getMovieId() {
        return movieId;
    }

    public ShowRequest() {
    }

    public ShowRequest(Date date, int slotId, BigDecimal cost, String movieId) {
        this.date = date;
        this.slotId = slotId;
        this.cost = cost;
        this.movieId = movieId;
    }
}

