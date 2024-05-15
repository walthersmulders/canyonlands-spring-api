package com.walthersmulders.mapstruct.dto.genre.music;

import java.util.UUID;

public record GenreMusic(
        UUID genreMusicID,
        String genre
) {
}
