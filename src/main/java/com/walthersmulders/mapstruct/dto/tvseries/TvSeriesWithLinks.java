package com.walthersmulders.mapstruct.dto.tvseries;

import com.walthersmulders.mapstruct.dto.genre.GenreTvSeries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TvSeriesWithLinks(
        UUID tvSeriesID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated,
        List<GenreTvSeries> genres
) {
}
