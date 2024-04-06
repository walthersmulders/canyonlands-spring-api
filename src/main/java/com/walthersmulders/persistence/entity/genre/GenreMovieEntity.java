package com.walthersmulders.persistence.entity.genre;

import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovieUpsert;
import com.walthersmulders.persistence.entity.movie.MovieGenreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Table containing the genres of movies.
 */
@Entity
@Table(name = "genre_movie")
@Getter
@Setter
@NoArgsConstructor
public class GenreMovieEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "genre_movie_id", nullable = false)
    private UUID genreMovieID;

    @Column(name = "genre", nullable = false, unique = true)
    private String genre;

    @Column(name = "external_id", nullable = false, unique = true)
    private Integer externalID;

    @OneToMany(mappedBy = "genreMovie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenreEntity> movieGenres;

    public boolean checkUpdateDtoEqualsEntity(GenreMovieUpsert genreMovieUpsert) {
        return this.genre.equals(genreMovieUpsert.genre()) &&
               this.externalID.equals(genreMovieUpsert.externalID());
    }
}
