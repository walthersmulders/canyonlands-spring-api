package com.walthersmulders.mapstruct.dto.book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Book(
        UUID bookID,
        String title,
        String isbn,
        Integer pages,
        String plot,
        String cover,
        LocalDate datePublished,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated
) {
}
