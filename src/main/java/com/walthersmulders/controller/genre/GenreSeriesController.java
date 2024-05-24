package com.walthersmulders.controller.genre;

import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeriesUpsert;
import com.walthersmulders.service.genre.GenreSeriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genres/series")
@RestController
@Validated
public class GenreSeriesController {
    private final GenreSeriesService genreSeriesService;

    public GenreSeriesController(GenreSeriesService genreSeriesService) {
        this.genreSeriesService = genreSeriesService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreSeries createGenre(@Valid @RequestBody GenreSeriesUpsert genreSeriesUpsert) {
        return genreSeriesService.create(genreSeriesUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreSeries> getGenres() {
        return genreSeriesService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreSeries getGenre(@PathVariable UUID id) {
        return genreSeriesService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(
            @PathVariable UUID id,
            @Valid @RequestBody GenreSeriesUpsert genreSeriesUpsert
    ) {
        genreSeriesService.update(id, genreSeriesUpsert);
    }

    // TODO :: Delete series genre functionality
}
