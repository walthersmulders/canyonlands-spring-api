package com.walthersmulders.mapstruct.dto.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BookWithLinksUpsert(
        @Valid @NotNull List<UUID> authorIDs,
        @NotNull UUID bookGenreID,
        @NotNull BookUpsert book
) {
}
