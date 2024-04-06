package com.walthersmulders.mapstruct.dto.genre.book;

import java.util.UUID;

public record GenreBook(
        UUID bookGenreID,
        String genre,
        String subGenre
) {
}
