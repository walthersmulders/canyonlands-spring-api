package com.walthersmulders.controller;

// TODO :: Controller to manage genres used for movies

import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genremovie.GenreMovieUpsert;
import com.walthersmulders.service.GenreMovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreMovie> getGenres() {
        return genreMovieService.getGenres();
    }
}
