package com.walthersmulders.mapstruct.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// TODO :: Rename to BookUpsert
public record BookAdd(
        @NotEmpty @Size(min = 1, max = 255) String title,
        @NotEmpty String plot,
        @NotEmpty @Size(min = 1, max = 20) String isbn,
        @NotNull Integer pages,
        @NotEmpty @Size(min = 1, max = 1000) String cover,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate datePublished
) {
}
