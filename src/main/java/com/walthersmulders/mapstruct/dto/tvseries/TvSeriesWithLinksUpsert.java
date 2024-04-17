package com.walthersmulders.mapstruct.dto.tvseries;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TvSeriesWithLinksUpsert(
        @Valid @NotNull List<UUID> genreIDs,
        @NotNull TvSeriesUpsert tvSeries
) {
}
