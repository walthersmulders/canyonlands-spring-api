package com.walthersmulders.mapstruct.dto.genre.movie;

import java.util.UUID;

public record GenreMovie(
        UUID genreMovieID,
        String genre
) {
}
