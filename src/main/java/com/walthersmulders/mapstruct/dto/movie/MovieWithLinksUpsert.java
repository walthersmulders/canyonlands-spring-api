package com.walthersmulders.mapstruct.dto.movie;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MovieWithLinksUpsert(
        @Valid @NotNull List<UUID> genreIDs,
        @NotNull MovieUpsert movie
) {
}
