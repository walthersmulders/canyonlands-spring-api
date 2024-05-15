package com.walthersmulders.persistence.repository.genre;

import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreSeriesRepository extends JpaRepository<GenreSeriesEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(gse) > 0 THEN true ELSE false END " +
           "FROM GenreSeriesEntity gse " +
           "WHERE gse.genre = :genre")
    boolean exists(
            @Param(value = "genre") String genre
    );
}
