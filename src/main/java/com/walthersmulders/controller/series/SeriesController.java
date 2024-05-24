package com.walthersmulders.controller.series;

import com.walthersmulders.mapstruct.dto.series.*;
import com.walthersmulders.service.series.SeriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/series")
@RestController
@Validated
public class SeriesController {
    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {this.seriesService = seriesService;}

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SeriesWithLinks createWithLinks(
            @Valid @RequestBody SeriesWithLinksUpsert seriesWithLinksUpsert
    ) {
        return seriesService.createWithLinks(seriesWithLinksUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Series> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/links")
    public List<SeriesWithLinks> getAllSeriesWithLinks() {
        return seriesService.getAllSeriesWithLinks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Series getSeries(@PathVariable UUID id) {
        return seriesService.getSeries(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/links")
    public SeriesWithLinks getSeriesWithLinks(@PathVariable UUID id) {
        return seriesService.getSeriesWithLinks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateSeries(
            @PathVariable UUID id,
            @Valid @RequestBody SeriesUpsert seriesUpsert
    ) {
        seriesService.updateSeries(id, seriesUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{seriesID}/genres/{genreID}")
    public void addGenre(@PathVariable UUID seriesID, @PathVariable UUID genreID) {
        seriesService.addGenre(seriesID, genreID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{seriesID}/genres/{genresID}")
    public void removeGenreFromTvSeries(@PathVariable UUID seriesID, @PathVariable UUID genresID) {
        seriesService.removeGenreFromSeries(seriesID, genresID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{seriesID}/season/{seasonID}")
    public void updateSeason(
            @PathVariable UUID seriesID,
            @PathVariable UUID seasonID,
            @Valid @RequestBody SeasonUpsert seasonUpsert
    ) {
        seriesService.updateSeason(seriesID, seasonID, seasonUpsert);
    }
}
