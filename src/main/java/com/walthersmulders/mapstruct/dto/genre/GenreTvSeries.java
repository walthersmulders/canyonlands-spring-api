package com.walthersmulders.mapstruct.dto.genre;

import java.util.UUID;

public record GenreTvSeries(
        UUID genreTvSeriesID,
        String genre,
        Integer externalID
) {
}
