package com.walthersmulders.controller.movie;

import com.walthersmulders.mapstruct.dto.movie.Movie;
import com.walthersmulders.mapstruct.dto.movie.MovieUpsert;
import com.walthersmulders.mapstruct.dto.movie.MovieWithLinks;
import com.walthersmulders.mapstruct.dto.movie.MovieWithLinksUpsert;
import com.walthersmulders.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/movies")
@RestController
@Validated
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieWithLinks createWithLinks(@Valid @RequestBody MovieWithLinksUpsert movieWithLinksUpsert) {
        return movieService.createMovieWithLinks(movieWithLinksUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getMovies();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/links")
    public List<MovieWithLinks> getMoviesWithLinks() {
        return movieService.getMoviesWithLinks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable UUID id) {
        return movieService.getMovie(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/links")
    public MovieWithLinks getMovieWithLinks(@PathVariable UUID id) {
        return movieService.getMovieWithLinks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateMovie(@PathVariable UUID id, @Valid @RequestBody MovieUpsert movieUpsert) {
        movieService.updateMovie(id, movieUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{movieID}/genres/{genreID}")
    public void addGenre(@PathVariable UUID movieID, @PathVariable UUID genreID) {
        movieService.addGenre(movieID, genreID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{movieID}/genres/{genreID}")
    public void removeGenreFromMovie(@PathVariable UUID movieID, @PathVariable UUID genreID) {
        movieService.removeGenreMovie(movieID, genreID);
    }
}
