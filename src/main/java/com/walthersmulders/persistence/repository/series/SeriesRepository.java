package com.walthersmulders.persistence.repository.series;

import com.walthersmulders.persistence.entity.series.SeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(se) > 0 THEN true ELSE false END " +
           "FROM SeriesEntity se " +
           "WHERE se.title = :title")
    boolean existsByTitle(
            @Param(value = "title") String title
    );

    @Query("SELECT se " +
           "FROM SeriesEntity se " +
           "LEFT JOIN FETCH se.seriesGenres")
    List<SeriesEntity> fetchSeriesWithLinks();

    @Query("SELECT se " +
           "FROM SeriesEntity se " +
           "LEFT JOIN FETCH se.seriesGenres " +
           "WHERE se.seriesID = :seriesID")
    Optional<SeriesEntity> fetchSeriesWithLinks(
            @Param(value = "seriesID") UUID seriesID
    );
}
