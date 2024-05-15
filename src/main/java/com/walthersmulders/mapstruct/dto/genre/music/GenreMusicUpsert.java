package com.walthersmulders.mapstruct.dto.genre.music;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record GenreMusicUpsert(
        @NotEmpty @Size(min = 1, max = 255) String genre
) {
}
