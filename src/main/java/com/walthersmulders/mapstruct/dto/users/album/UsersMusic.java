package com.walthersmulders.mapstruct.dto.users.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.album.Album;

public record UsersMusic(
        Album album,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String review,
        Integer rating
) {
}
