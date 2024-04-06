package com.walthersmulders.mapstruct.dto.movie;

import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MovieWithLinks(
        UUID movieID,
        String title,
        String plot,
        String poster,
        Integer externalID,
        LocalDate dateReleased,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated,
        List<GenreMovie> genres
) {
}
