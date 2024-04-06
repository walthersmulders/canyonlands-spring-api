package com.walthersmulders.controller.genre;

import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovieUpsert;
import com.walthersmulders.service.GenreMovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genres/movies")
@RestController
@Validated
public class GenreMovieController {
    private final GenreMovieService genreMovieService;

    public GenreMovieController(GenreMovieService genreMovieService) {
        this.genreMovieService = genreMovieService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreMovie createGenre(@Valid @RequestBody GenreMovieUpsert genreMovieUpsert) {
        return genreMovieService.create(genreMovieUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreMovie> getGenres() {
        return genreMovieService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreMovie getGenre(@PathVariable UUID id) {
        return genreMovieService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(
            @PathVariable UUID id,
            @Valid @RequestBody GenreMovieUpsert genreMovieUpsert
    ) {
        genreMovieService.update(id, genreMovieUpsert);
    }
}
