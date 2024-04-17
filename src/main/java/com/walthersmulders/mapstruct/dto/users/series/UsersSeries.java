package com.walthersmulders.mapstruct.dto.users.series;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.series.Series;

public record UsersSeries(
        Series series,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String review,
        Integer rating
) {
}
