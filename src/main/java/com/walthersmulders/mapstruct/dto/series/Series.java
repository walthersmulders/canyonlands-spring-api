package com.walthersmulders.mapstruct.dto.series;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Series(
        UUID seriesID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
