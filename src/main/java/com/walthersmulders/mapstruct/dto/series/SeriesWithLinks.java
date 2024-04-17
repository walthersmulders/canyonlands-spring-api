package com.walthersmulders.mapstruct.dto.series;

import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SeriesWithLinks(
        UUID seriesID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated,
        List<GenreSeries> genres
) {
}
