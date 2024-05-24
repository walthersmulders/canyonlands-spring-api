package com.walthersmulders.mapstruct.dto.series;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Season(
        UUID seasonID,
        String title,
        String plot,
        String poster,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
