package com.walthersmulders.mapstruct.dto.genre;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GenreTvSeriesUpsert(
        @NotEmpty @Size(min = 1, max = 255) String genre,
        @NotNull Integer externalID
) {
}
