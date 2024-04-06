package com.walthersmulders.persistence.entity.movie;


import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Table containing the mapping between movies and genres.
 * A movie can have multiple genres, and a genre can be assigned to multiple movies.
 */
@Entity
@Table(name = "movie_genre")
@Getter
@Setter
@NoArgsConstructor
public class MovieGenreEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MovieGenreID movieGenreID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreMovieID")
    @JoinColumn(name = "genre_movie_id")
    private GenreMovieEntity genreMovie;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieID")
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public MovieGenreEntity(GenreMovieEntity genreMovie, MovieEntity movie) {
        this.genreMovie = genreMovie;
        this.movie = movie;
        this.movieGenreID = new MovieGenreID(genreMovie.getGenreMovieID(), movie.getMovieID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreMovie, movie);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieGenreEntity that = (MovieGenreEntity) o;

        return Objects.equals(genreMovie, that.genreMovie) && Objects.equals(movie, that.movie);
    }
}
