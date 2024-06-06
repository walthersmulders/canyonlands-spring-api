package com.walthersmulders.controller.music;

import com.walthersmulders.mapstruct.dto.album.Album;
import com.walthersmulders.mapstruct.dto.album.AlbumUpsert;
import com.walthersmulders.mapstruct.dto.album.AlbumWithLinks;
import com.walthersmulders.mapstruct.dto.album.AlbumWithLinksUpsert;
import com.walthersmulders.service.music.ArtistAlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/albums")
@RestController
@Validated
public class AlbumController {
    private final ArtistAlbumService artistAlbumService;

    public AlbumController(ArtistAlbumService artistAlbumService) {
        this.artistAlbumService = artistAlbumService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AlbumWithLinks createWithArtists(@Valid @RequestBody AlbumWithLinksUpsert albumWithLinksUpsert) {
        return artistAlbumService.createAlbumWithArtists(albumWithLinksUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Album> getAlbums() {
        return artistAlbumService.getAlbums();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/links")
    public List<AlbumWithLinks> getAlbumsWithLinks() {
        return artistAlbumService.getAlbumsWithLinks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Album getAlbum(@PathVariable UUID id) {
        return artistAlbumService.getAlbum(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/links")
    public AlbumWithLinks getAlbumWithLinks(@PathVariable UUID id) {
        return artistAlbumService.getAlbumWithLinks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateAlbum(@PathVariable UUID id, @Valid @RequestBody AlbumUpsert albumUpsert) {
        artistAlbumService.updateAlbum(id, albumUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{albumID}/artists/{artistID}")
    public void addArtistToAlbum(@PathVariable UUID albumID, @PathVariable UUID artistID) {
        artistAlbumService.addArtistToAlbum(albumID, artistID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{albumID}/artists/{artistID}")
    public void removeArtistFromAlbum(@PathVariable UUID albumID, @PathVariable UUID artistID) {
        artistAlbumService.removeArtistFromAlbum(albumID, artistID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{albumID}/genres/{genreID}")
    public void updateAlbumGenre(@PathVariable UUID albumID, @PathVariable UUID genreID) {
        artistAlbumService.updateAlbumGenre(albumID, genreID);
    }

}
