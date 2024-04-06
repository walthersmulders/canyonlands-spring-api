package com.walthersmulders.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieGenreID implements Serializable {
    @Column(name = "genre_movie_id", nullable = false)
    private UUID genreMovieID;

    @Column(name = "movie_id", nullable = false)
    private UUID movieID;

    @Override
    public int hashCode() {
        return Objects.hash(genreMovieID, movieID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieGenreID that = (MovieGenreID) o;

        return Objects.equals(genreMovieID, that.genreMovieID) &&
               Objects.equals(movieID, that.movieID);
    }
}
