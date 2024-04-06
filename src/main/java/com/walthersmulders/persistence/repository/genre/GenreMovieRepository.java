package com.walthersmulders.persistence.repository.genre;

import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreMovieRepository extends JpaRepository<GenreMovieEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(gme) > 0 THEN true ELSE false END " +
           "FROM GenreMovieEntity gme " +
           "WHERE gme.genre = :genre " +
           "OR gme.externalID = :externalID")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "externalID") Integer externalID
    );
}
