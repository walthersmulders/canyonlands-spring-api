package com.walthersmulders.controller.genre;

import com.walthersmulders.mapstruct.dto.genre.music.GenreMusic;
import com.walthersmulders.mapstruct.dto.genre.music.GenreMusicUpsert;
import com.walthersmulders.service.genre.GenreMusicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genres/music")
@RestController
@Validated
public class GenreMusicController {
    private final GenreMusicService genreMusicService;

    public GenreMusicController(GenreMusicService genreMusicService) {
        this.genreMusicService = genreMusicService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreMusic create(@Valid @RequestBody GenreMusicUpsert genreMusicUpsert) {
        return genreMusicService.create(genreMusicUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreMusic> getAll() {
        return genreMusicService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreMusic getGenre(@PathVariable UUID id) {
        return genreMusicService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(@PathVariable UUID id, @Valid @RequestBody GenreMusicUpsert genreMusicUpsert) {
        genreMusicService.update(id, genreMusicUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        genreMusicService.delete(id);
    }
}
