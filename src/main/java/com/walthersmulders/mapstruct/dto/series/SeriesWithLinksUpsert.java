package com.walthersmulders.mapstruct.dto.series;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SeriesWithLinksUpsert(
        @Valid @NotNull List<UUID> genreIDs,
        @NotNull SeriesUpsert series
) {
}
