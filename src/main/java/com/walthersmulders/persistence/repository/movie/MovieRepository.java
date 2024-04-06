package com.walthersmulders.persistence.repository.movie;

import com.walthersmulders.persistence.entity.movie.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(me) > 0 THEN true ELSE false END " +
           "FROM MovieEntity me " +
           "WHERE me.title = :title")
    boolean existsByTitle(
            @Param(value = "title") String title
    );

    @Query("SELECT CASE WHEN COUNT(me) > 0 THEN true ELSE false END " +
           "FROM MovieEntity me " +
           "WHERE me.externalID = :externalID")
    boolean existsByExternalID(
            @Param(value = "externalID") Integer externalID
    );

    @Query("SELECT me " +
           "FROM MovieEntity me " +
           "LEFT JOIN FETCH me.movieGenres")
    List<MovieEntity> fetchMoviesWithLinks();

    @Query("SELECT me " +
           "FROM MovieEntity me " +
           "LEFT JOIN FETCH me.movieGenres " +
           "WHERE me.movieID = :movieID")
    Optional<MovieEntity> fetchMovieWithLinks(
            @Param(value = "movieID") UUID movieID
    );
}
