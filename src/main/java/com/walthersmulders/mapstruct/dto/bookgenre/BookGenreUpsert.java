package com.walthersmulders.mapstruct.dto.bookgenre;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// TODO :: Rename to BookGenreUpsert
public record BookGenreUpsert(
        @NotEmpty @Size(min = 1, max = 255) String genre,
        @NotEmpty @Size(min = 1, max = 500) String subGenre
) {
}
