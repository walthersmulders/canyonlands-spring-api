package com.walthersmulders.controller.tvseries;

import com.walthersmulders.mapstruct.dto.tvseries.TvSeries;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesUpsert;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinks;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinksUpsert;
import com.walthersmulders.service.tvseries.TvSeriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/tv-series")
@RestController
@Validated
public class TvSeriesController {
    private final TvSeriesService tvSeriesService;

    public TvSeriesController(TvSeriesService tvSeriesService) {this.tvSeriesService = tvSeriesService;}

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TvSeriesWithLinks createWithLinks(
            @Valid @RequestBody TvSeriesWithLinksUpsert tvSeriesWithLinksUpsert
    ) {
        return tvSeriesService.createWithLinks(tvSeriesWithLinksUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TvSeries> getAllTvSeries() {
        return tvSeriesService.getAllTvSeries();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/links")
    public List<TvSeriesWithLinks> getAllTvSeriesWithLinks() {
        return tvSeriesService.getAllTvSeriesWithLinks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public TvSeries getTvSeries(@PathVariable UUID id) {
        return tvSeriesService.getTvSeries(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/links")
    public TvSeriesWithLinks getTvSeriesWithLinks(@PathVariable UUID id) {
        return tvSeriesService.getTvSeriesWithLinks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateTvSeries(
            @PathVariable UUID id,
            @Valid @RequestBody TvSeriesUpsert tvSeriesUpsert
    ) {
        tvSeriesService.updateTvSeries(id, tvSeriesUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{tvSeriesID}/genres/{genreID}")
    public void addGenre(@PathVariable UUID tvSeriesID, @PathVariable UUID genreID) {
        tvSeriesService.addGenre(tvSeriesID, genreID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{tvSeriesID}/genres/{genresID}")
    public void removeGenreFromTvSeries(@PathVariable UUID tvSeriesID, @PathVariable UUID genresID) {
        tvSeriesService.removeGenreFromTvSeries(tvSeriesID, genresID);
    }
}
