package com.walthersmulders.mapstruct.dto.artist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ArtistUpsert(
        @NotEmpty @Size(min = 1, max = 100) String firstName,
        @NotEmpty @Size(min = 1, max = 100) String lastName,
        @Size(max = 100) String additionalName
) {
}
