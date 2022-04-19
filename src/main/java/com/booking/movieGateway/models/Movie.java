package com.booking.movieGateway.models;

import com.booking.utilities.serializers.duration.DurationSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Duration;
import java.util.Objects;

@ApiModel(value = "Movie")
public class Movie {

    @JsonProperty
    @ApiModelProperty(name = "id", value = "The movie id", example = "title_1", position = 1)
    private final String id;

    @JsonProperty
    @ApiModelProperty(name = "name", value = "Name of the movie", required = true, example = "Movie", position = 2)
    private final String name;

    @JsonProperty
    @JsonSerialize(using = DurationSerializer.class)
    @ApiModelProperty(name = "name", dataType = "java.lang.String", value = "Duration of the movie", required = true, example = "1h 30m", position = 3)
    private final Duration duration;

    @JsonProperty
    @ApiModelProperty(name = "description", value = "Description of the movie", required = true, example = "Movie Description", position = 4)
    private final String plot;

    @JsonProperty
    @ApiModelProperty(name = "poster", value = "Poster link of the movie", required = true, example = "https://m.media-amazon.com/images/M/MV5BMjI0MDMzNTQ0M15BMl5BanBnXkFtZTgwMTM5NzM3NDM@._V1_SX300.jpg", position = 5)
    private final String posterLink;

    @JsonProperty
    @ApiModelProperty(name = "imdbRating", value = "Rating of the movie", required = true, example = "6.9", position = 6)
    private final String imdbRating;

    public Movie(String id, String name, Duration duration, String plot, String posterLink, String imdbRating) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.plot = plot;
        this.posterLink = posterLink;
        this.imdbRating = imdbRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id.equals(movie.id) &&
                name.equals(movie.name) &&
                duration.equals(movie.duration) &&
                plot.equals(movie.plot) &&
                imdbRating.equals(movie.imdbRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, duration, plot, posterLink, imdbRating);
    }
}
