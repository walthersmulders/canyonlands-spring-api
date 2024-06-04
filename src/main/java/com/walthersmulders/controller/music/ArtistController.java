package com.walthersmulders.controller.music;

import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import com.walthersmulders.service.music.ArtistAlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/artists")
@RestController
@Validated
public class ArtistController {
    private final ArtistAlbumService artistAlbumService;

    public ArtistController(ArtistAlbumService artistAlbumService) {
        this.artistAlbumService = artistAlbumService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Artist create(@Valid @RequestBody ArtistUpsert artist) {
        return artistAlbumService.createArtist(artist);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Artist> getArtists() {
        return artistAlbumService.getArtists();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/albumbs")
    public List<ArtistWithAlbums> getArtistsWithAlbums() {
        return artistAlbumService.getArtistsWithAlbums();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Artist getArtist(@PathVariable UUID id) {
        return artistAlbumService.getArtist(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/albums")
    public ArtistWithAlbums getArtistWithAlbums(@PathVariable UUID id) {
        return artistAlbumService.getArtistWithAlbums(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateArtist(@PathVariable UUID id, @Valid @RequestBody ArtistUpsert artist) {
        artistAlbumService.updateArtist(id, artist);
    }
}
