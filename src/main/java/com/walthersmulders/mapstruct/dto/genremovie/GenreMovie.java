package com.walthersmulders.mapstruct.dto.genremovie;

import java.util.UUID;

public record GenreMovie(
        UUID genreMovieID,
        String genre,
        Integer externalID
) {
}
