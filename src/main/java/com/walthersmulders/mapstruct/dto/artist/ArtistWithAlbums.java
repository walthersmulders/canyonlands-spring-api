package com.walthersmulders.mapstruct.dto.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.album.Album;

import java.util.List;
import java.util.UUID;

public record ArtistWithAlbums(
        UUID artistID,
        String firstName,
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String additionalName,
        List<Album> albums
) {
}
