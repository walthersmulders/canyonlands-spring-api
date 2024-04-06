package com.walthersmulders.mapstruct.dto.genremovie;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GenreMovieUpsert(
        @NotEmpty @Size(min = 1, max = 255) String genre,
        @NotNull Integer externalID
) {
}
