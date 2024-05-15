package com.walthersmulders.mapstruct.dto.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlbumUpsert(
        @NotEmpty @Size(min = 1, max = 255) String title,
        @NotEmpty @Size(min = 1, max = 1000) String cover,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate datePublished
) {
}
