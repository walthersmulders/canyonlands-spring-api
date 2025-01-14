package com.walthersmulders.service.series;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.series.*;
import com.walthersmulders.mapstruct.mapper.SeasonMapper;
import com.walthersmulders.mapstruct.mapper.SeriesMapper;
import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import com.walthersmulders.persistence.entity.series.SeasonEntity;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.series.SeriesGenreEntity;
import com.walthersmulders.persistence.repository.genre.GenreSeriesRepository;
import com.walthersmulders.persistence.repository.series.SeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class SeriesService {
    private static final String SERIES    = "SERIES";
    private static final String SERIES_ID = "SERIES_ID";

    private final SeriesRepository      seriesRepository;
    private final SeriesMapper          seriesMapper;
    private final SeasonMapper          seasonMapper;
    private final GenreSeriesRepository genreSeriesRepository;

    public SeriesService(
            SeriesRepository seriesRepository,
            SeriesMapper seriesMapper,
            SeasonMapper seasonMapper,
            GenreSeriesRepository genreSeriesRepository
    ) {
        this.seriesRepository = seriesRepository;
        this.seriesMapper = seriesMapper;
        this.seasonMapper = seasonMapper;
        this.genreSeriesRepository = genreSeriesRepository;
    }

    @Transactional
    public SeriesWithLinks createWithLinks(SeriesWithLinksUpsert seriesWithLinksUpsert) {
        log.info("Creating Series with links");
        log.info("Check if series with title already exists");

        boolean existsByTitle = seriesRepository.existsByTitle(seriesWithLinksUpsert.series().title());

        if (existsByTitle) {
            throw new EntityExistsException(
                    SERIES, Map.of("title", "Series with title already exists")
            );
        }

        // Will throw error if any of the IDs supplied are invalid.
        List<GenreSeriesEntity> genreSeries = genreSeriesRepository.findAllById(
                seriesWithLinksUpsert.genreIDs()
        );

        SeriesEntity series = seriesMapper.seriesUpsertToEntity(seriesWithLinksUpsert.series());

        series.setDateAdded(LocalDateTime.now());
        series.setDateUpdated(LocalDateTime.now());

        genreSeries.forEach(series::addSeriesGenre);

        if (series.getSeriesGenres().isEmpty()) {
            log.error("Series must have at least one genre");

            throw new GenericBadRequestException("Series must have at least one genre");
        }

        List<SeasonEntity> seasons = new ArrayList<>();

        seriesWithLinksUpsert.seasons().forEach(item -> {
            SeasonEntity season = seasonMapper.seasonUpsertToEntity(item);
            season.setDateAdded(LocalDateTime.now());
            season.setDateUpdated(LocalDateTime.now());
            seasons.add(season);
        });

        if (seasons.isEmpty()) {
            log.error("Series must have at least one season");

            throw new GenericBadRequestException("Series must have at least one season");
        }

        series.addSeasons(seasons);

        seriesRepository.save(series);

        return seriesMapper.entityToSeriesWithLinks(series);
    }

    public List<Series> getAllSeries() {
        log.info("Getting all series");

        List<SeriesEntity> series = seriesRepository.findAll();

        log.info("Found {} series", series.size());

        return series.isEmpty() ? Collections.emptyList()
                                : series.stream()
                                        .map(seriesMapper::entityToSeries)
                                        .toList();
    }

    @Transactional(readOnly = true)
    public List<SeriesWithLinks> getAllSeriesWithLinks() {
        log.info("Getting all series with links");

        List<SeriesEntity> seriesWithLinks = seriesRepository.fetchSeriesWithLinks();

        log.info("Found {} series with links", seriesWithLinks.size());

        return seriesWithLinks.isEmpty() ? Collections.emptyList()
                                         : seriesWithLinks.stream()
                                                          .map(seriesMapper::entityToSeriesWithLinks)
                                                          .toList();
    }

    public Series getSeries(UUID id) {
        log.info("Getting series with id {}", id);

        SeriesEntity series = seriesRepository.findById(id)
                                              .orElseThrow(() -> new EntityNotFoundException(
                                                      SERIES,
                                                      Map.of(SERIES_ID, id.toString())
                                              ));

        return seriesMapper.entityToSeries(series);
    }

    @Transactional(readOnly = true)
    public SeriesWithLinks getSeriesWithLinks(UUID id) {
        log.info("Getting series with links with id: {}", id);

        SeriesEntity series = seriesRepository.fetchSeriesWithLinks(id)
                                              .orElseThrow(() -> new EntityNotFoundException(
                                                      SERIES,
                                                      Map.of(SERIES_ID, id.toString())
                                              ));

        return seriesMapper.entityToSeriesWithLinks(series);
    }

    public void updateSeries(UUID id, SeriesUpsert seriesUpsert) {
        log.info("Updating series with id {}", id);

        Optional<SeriesEntity> existingSeries = seriesRepository.findById(id);

        if (existingSeries.isEmpty()) {
            log.error("Series with id {} not found", id);

            throw new EntityNotFoundException(
                    SERIES,
                    Map.of(SERIES_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingSeries.get().checkUpdateDtoEqualsEntity(seriesUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields, updating series");

            if (!existingSeries.get().getTitle().equals(seriesUpsert.title())) {
                log.info("Check if series with title already exists");

                boolean existsByTitle = seriesRepository.existsByTitle(seriesUpsert.title());

                if (existsByTitle) {
                    throw new EntityExistsException(
                            SERIES,
                            Map.of(SERIES_ID, id.toString())
                    );
                }
            }

            SeriesEntity updatedSeries = seriesMapper.seriesEntityUpdateMerge(
                    existingSeries.get(),
                    seriesUpsert
            );

            updatedSeries.setDateUpdated(LocalDateTime.now());

            seriesRepository.save(updatedSeries);

            log.info("Series with id {} has been updated", id);
        }
    }

    @Transactional
    public void addGenre(UUID seriesID, UUID genreID) {
        log.info("Adding genre with id {} to series with id {}", genreID, seriesID);

        Optional<SeriesEntity> series = seriesRepository.fetchSeriesWithLinks(seriesID);

        if (series.isEmpty()) {
            log.error("Series with seriesID {} not found", seriesID);

            throw new EntityNotFoundException(
                    SERIES,
                    Map.of(SERIES_ID, seriesID.toString())
            );
        }

        SeriesGenreEntity seriesGenre = series.get().getSeriesGenres()
                                              .stream()
                                              .filter(genre -> genre.getGenreSeries().getGenreSeriesID().equals(genreID))
                                              .findFirst()
                                              .orElse(null);

        if (seriesGenre != null) {
            log.error("Series with seriesID {} already has a genre with genreID {}", seriesID, genreID);

            throw new EntityExistsException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        Optional<GenreSeriesEntity> genreSeries = genreSeriesRepository.findById(genreID);

        if (genreSeries.isEmpty()) {
            log.error("Genre with genreID {} not found", genreID);

            throw new EntityNotFoundException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        series.get().addSeriesGenre(genreSeries.get());

        seriesRepository.save(series.get());

        log.info("Genre with id {} added to series with id {}", genreID, seriesID);
    }

    @Transactional
    public void removeGenreFromSeries(UUID seriesID, UUID genresID) {
        log.info("Removing genre with id {} from series with id {}", genresID, seriesID);

        Optional<SeriesEntity> series = seriesRepository.fetchSeriesWithLinks(seriesID);

        if (series.isEmpty()) {
            log.error("Series with seriesID {} not found", seriesID);

            throw new EntityNotFoundException(
                    SERIES,
                    Map.of(SERIES_ID, seriesID.toString())
            );
        }

        SeriesGenreEntity seriesGenre = series.get().getSeriesGenres()
                                              .stream()
                                              .filter(genre -> genre.getGenreSeries().getGenreSeriesID().equals(genresID))
                                              .findFirst()
                                              .orElse(null);

        if (seriesGenre != null) {
            if (seriesGenre.getSeries().getSeriesGenres().size() <= 1) {
                log.error("A Series must have at least one genre");

                throw new GenericBadRequestException("A Series must have at least one genre");
            }

            seriesGenre.getSeries().removeSeriesGenre(seriesGenre.getGenreSeries());
        }
    }

    @Transactional
    public void updateSeason(UUID seriesID, UUID seasonID, SeasonUpsert seasonUpsert) {
        // TODO impl
        log.info("TODO :: Implement this");
    }
}
