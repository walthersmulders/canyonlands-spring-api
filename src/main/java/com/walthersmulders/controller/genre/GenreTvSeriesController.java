package com.walthersmulders.controller.genre;

import com.walthersmulders.mapstruct.dto.genre.GenreTvSeries;
import com.walthersmulders.mapstruct.dto.genre.GenreTvSeriesUpsert;
import com.walthersmulders.service.genre.GenreTvSeriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genres/tv-series")
@RestController
@Validated
public class GenreTvSeriesController {
    private final GenreTvSeriesService genreTvSeriesService;

    public GenreTvSeriesController(GenreTvSeriesService genreTvSeriesService) {
        this.genreTvSeriesService = genreTvSeriesService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreTvSeries createGenre(@Valid @RequestBody GenreTvSeriesUpsert genreTvSeriesUpsert) {
        return genreTvSeriesService.create(genreTvSeriesUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreTvSeries> getGenres() {
        return genreTvSeriesService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreTvSeries getGenre(@PathVariable UUID id) {
        return genreTvSeriesService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(
            @PathVariable UUID id,
            @Valid @RequestBody GenreTvSeriesUpsert genreTvSeriesUpsert
    ) {
        genreTvSeriesService.update(id, genreTvSeriesUpsert);
    }
}
