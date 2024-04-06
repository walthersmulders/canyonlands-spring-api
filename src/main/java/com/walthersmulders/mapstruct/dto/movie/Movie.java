package com.walthersmulders.mapstruct.dto.movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Movie(
        UUID movieID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
