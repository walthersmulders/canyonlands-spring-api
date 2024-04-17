package com.walthersmulders.persistence.repository.tvseries;

import com.walthersmulders.persistence.entity.tvseries.TvSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TvSeriesRepository extends JpaRepository<TvSeriesEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(tse) > 0 THEN true ELSE false END " +
           "FROM TvSeriesEntity tse " +
           "WHERE tse.title = :title")
    boolean existsByTitle(
            @Param(value = "title") String title
    );

    @Query("SELECT CASE WHEN COUNT(tse) > 0 THEN true ELSE false END " +
           "FROM TvSeriesEntity tse " +
           "WHERE tse.externalID = :externalID")
    boolean existsByExternalID(
            @Param(value = "externalID") Integer externalID
    );

    @Query("SELECT tse " +
           "FROM TvSeriesEntity tse " +
           "LEFT JOIN FETCH tse.tvSeriesGenres")
    List<TvSeriesEntity> fetchTvSeriesWithLinks();

    @Query("SELECT tse " +
           "FROM TvSeriesEntity tse " +
           "LEFT JOIN FETCH tse.tvSeriesGenres " +
           "WHERE tse.tvSeriesID = :tvSeriesID")
    Optional<TvSeriesEntity> fetchTvSeriesWithLinks(
            @Param(value = "tvSeriesID") UUID tvSeriesID
    );
}
