package com.walthersmulders.mapstruct.dto.tvseries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TvSeries(
        UUID tvSeriesID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
