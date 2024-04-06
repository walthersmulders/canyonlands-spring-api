package com.walthersmulders.mapstruct.dto.genre.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record GenreBookUpsert(
        @NotEmpty @Size(min = 1, max = 255) String genre,
        @NotEmpty @Size(min = 1, max = 500) String subGenre
) {
}
