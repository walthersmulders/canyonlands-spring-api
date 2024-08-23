package com.walthersmulders.mapstruct.dto.users.album;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsersAlbumUpsert(
        @Size(min = 1, max = 5000) String review,
        @NotNull @Min(1) @Max(10) Integer rating
) {
}