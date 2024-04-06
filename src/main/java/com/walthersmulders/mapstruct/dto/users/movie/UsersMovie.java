package com.walthersmulders.mapstruct.dto.users.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.movie.Movie;

public record UsersMovie(
        Movie movie,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String review,
        Integer rating
) {
}
