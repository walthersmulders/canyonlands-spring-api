package com.walthersmulders.service.tvseries;

import com.walthersmulders.mapstruct.dto.tvseries.TvSeries;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesUpsert;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinks;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinksUpsert;
import com.walthersmulders.mapstruct.mapper.TvSeriesMapper;
import com.walthersmulders.persistence.repository.genre.GenreTvSeriesRepository;
import com.walthersmulders.persistence.repository.tvseries.TvSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TvSeriesService {
    private static final String TV_SERIES    = "TV_SERIES";
    private static final String TV_SERIES_ID = "TV_SERIES_ID";

    private final TvSeriesRepository      tvSeriesRepository;
    private final TvSeriesMapper          tvSeriesMapper;
    private final GenreTvSeriesRepository genreTvSeriesRepository;

    public TvSeriesService(
            TvSeriesRepository tvSeriesRepository,
            TvSeriesMapper tvSeriesMapper,
            GenreTvSeriesRepository genreTvSeriesRepository
    ) {
        this.tvSeriesRepository = tvSeriesRepository;
        this.tvSeriesMapper = tvSeriesMapper;
        this.genreTvSeriesRepository = genreTvSeriesRepository;
    }

    public TvSeriesWithLinks createWithLinks(TvSeriesWithLinksUpsert tvSeriesWithLinksUpsert) {
        return null;
    }

    public List<TvSeries> getAllTvSeries() {
        return null;
    }

    public List<TvSeriesWithLinks> getAllTvSeriesWithLinks() {
        return null;
    }

    public TvSeries getTvSeries(UUID id) {
        return null;
    }

    public TvSeriesWithLinks getTvSeriesWithLinks(UUID id) {
        return null;
    }

    public void updateTvSeries(UUID id, TvSeriesUpsert tvSeriesUpsert) {

    }

    public void addGenre(UUID tvSeriesID, UUID genreID) {

    }

    public void removeGenreFromTvSeries(UUID tvSeriesID, UUID genresID) {

    }
}
