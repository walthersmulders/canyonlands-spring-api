package com.walthersmulders.mapstruct.dto.album;

import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.genre.music.GenreMusic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AlbumWithLinks(
        UUID albumID,
        String title,
        String cover,
        LocalDate datePublished,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated,
        List<Artist> artists,
        GenreMusic genreMusic
) {
}
