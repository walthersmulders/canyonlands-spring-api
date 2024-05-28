package com.walthersmulders.persistence.repository.series;

import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.series.SeriesGenreEntity;
import com.walthersmulders.persistence.entity.series.SeriesGenreID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeriesGenreRepository extends JpaRepository<SeriesGenreEntity, SeriesGenreID> {
    @Query("SELECT CASE WHEN COUNT(sge) > 0 THEN true ELSE false END " +
           "FROM SeriesGenreEntity sge " +
           "WHERE sge.seriesGenreID.genreSeriesID = :genreSeriesID")
    boolean existsByGenreSeriesID(
            @Param(value = "genreSeriesID") UUID genreSeriesID
    );

    @Query("SELECT DISTINCT(sge.series) " +
           "FROM SeriesGenreEntity sge " +
           "WHERE sge.seriesGenreID.genreSeriesID = :genreSeriesID")
    List<SeriesEntity> fetchDistinctSeriesForGenre(
            @Param(value = "genreSeriesID") UUID genreSeriesID
    );
}
