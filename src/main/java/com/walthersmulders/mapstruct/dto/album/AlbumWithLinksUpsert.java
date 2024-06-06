package com.walthersmulders.mapstruct.dto.album;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AlbumWithLinksUpsert(
        @Valid @NotNull List<UUID> artistIDs,
        @NotNull UUID genreMusicID,
        @NotNull AlbumUpsert album
) {
}
