package com.walthersmulders.mapstruct.dto.bookgenre;

import java.util.UUID;

public record BookGenre(
    UUID bookGenreID,
    String genre,
    String subGenre
){
}
