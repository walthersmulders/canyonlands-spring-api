package com.walthersmulders.mapstruct.dto.tvseries;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TvSeriesUpsert(
        @NotEmpty @Size(min = 1, max = 500) String title,
        @NotEmpty String plot,
        @NotEmpty @Size(min = 1, max = 1000) String poster,
        @NotNull Integer externalID,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateReleased
) {
}
