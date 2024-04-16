package com.walthersmulders.persistence.repository.genre;

import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreTvSeriesRepository extends JpaRepository<GenreTvSeriesEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(gtse) > 0 THEN true ELSE false END " +
           "FROM GenreTvSeriesEntity gtse " +
           "WHERE gtse.genre = :genre " +
           "OR gtse.externalID = :externalID")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "externalID") Integer externalID
    );
}
