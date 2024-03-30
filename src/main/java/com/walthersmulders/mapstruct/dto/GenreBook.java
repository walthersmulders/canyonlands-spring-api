package com.walthersmulders.mapstruct.dto;

import java.util.UUID;

public record GenreBook (
    UUID bookGenreID,
    String genre,
    String subGenre
){
}
