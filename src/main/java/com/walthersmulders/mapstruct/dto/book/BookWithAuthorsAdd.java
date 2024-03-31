package com.walthersmulders.mapstruct.dto.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

// TODO :: Rename to BookWithLinksUpsert
public record BookWithAuthorsAdd(
        @Valid @NotNull List<UUID> authorIDs,
        @NotNull UUID bookGenreID,
        @NotNull BookAdd book
) {
}
