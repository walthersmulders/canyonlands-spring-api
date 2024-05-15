package com.walthersmulders.mapstruct.dto.album;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Album(
        UUID albumID,
        String title,
        String cover,
        LocalDate datePublished,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
