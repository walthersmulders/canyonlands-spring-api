package com.walthersmulders.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
public class MovieEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "movie_id", nullable = false)
    private UUID movieID;

    @Column(name = "title", unique = true, nullable = false, length = 500)
    private String title;

    @Column(name = "plot", nullable = false)
    private String plot;

    @Column(name = "poster", nullable = false, length = 1000)
    private String poster;

    @Column(name = "external_id", nullable = false, unique = true)
    private Integer externalID;

    @Column(name = "date_added", nullable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @Column(name = "date_released", nullable = false)
    private LocalDate dateReleased;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenreEntity> movieGenres = new ArrayList<>();

    public void addMovieGenre(GenreMovieEntity genreMovie) {
        MovieGenreEntity movieGenre = new MovieGenreEntity(genreMovie, this);
        movieGenres.add(movieGenre);
    }

    // TODO :: remove movieGenre link helper method

    @Override
    public int hashCode() {
        return Objects.hash(this.movieID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return movieID != null && movieID.equals(((MovieEntity) o).movieID);
    }
}
