package com.walthersmulders.mapstruct.dto.genre.series;

import java.util.UUID;

public record GenreSeries(
        UUID genreSeriesID,
        String genre,
        Integer externalID
) {
}
