package com.walthersmulders.persistence.repository.movie;

import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.movie.MovieGenreEntity;
import com.walthersmulders.persistence.entity.movie.MovieGenreID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenreEntity, MovieGenreID> {
    @Query("SELECT CASE WHEN COUNT(mge) > 0 THEN true ELSE false END " +
           "FROM MovieGenreEntity mge " +
           "WHERE mge.movieGenreID.genreMovieID = :genreMovieID")
    boolean existsByGenreMovieID(
            @Param(value = "genreMovieID") UUID genreMovieID
    );

    @Query("SELECT DISTINCT(mge.movie) " +
           "FROM MovieGenreEntity mge " +
           "WHERE mge.movieGenreID.genreMovieID = :genreMovieID")
    List<MovieEntity> fetchDistinctMoviesForGenre(
            @Param(value = "genreMovieID") UUID genreMovieID
    );
}
