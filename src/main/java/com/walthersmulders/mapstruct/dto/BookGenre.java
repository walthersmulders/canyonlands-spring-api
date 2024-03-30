package com.walthersmulders.mapstruct.dto;

import java.util.UUID;

public record BookGenre(
    UUID bookGenreID,
    String genre,
    String subGenre
){
}
