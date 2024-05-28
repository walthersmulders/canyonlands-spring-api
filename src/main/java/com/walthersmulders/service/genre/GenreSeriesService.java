package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeriesUpsert;
import com.walthersmulders.mapstruct.mapper.GenreSeriesMapper;
import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.repository.genre.GenreSeriesRepository;
import com.walthersmulders.persistence.repository.series.SeriesGenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class GenreSeriesService {
    private static final String GENRE_SERIES = "Genre:Series";

    private final GenreSeriesRepository genreSeriesRepository;
    private final GenreSeriesMapper     genreSeriesMapper;
    private final SeriesGenreRepository seriesGenreRepository;

    public GenreSeriesService(
            GenreSeriesRepository genreSeriesRepository,
            GenreSeriesMapper genreSeriesMapper,
            SeriesGenreRepository seriesGenreRepository
    ) {
        this.genreSeriesRepository = genreSeriesRepository;
        this.genreSeriesMapper = genreSeriesMapper;
        this.seriesGenreRepository = seriesGenreRepository;
    }

    public GenreSeries create(GenreSeriesUpsert genreSeriesUpsert) {
        log.info("Creating genre:series");

        boolean exists = genreSeriesRepository.exists(
                genreSeriesUpsert.genre()
        );

        if (exists) {
            log.error(
                    "Genre:series with genre {} already exists",
                    genreSeriesUpsert.genre()
            );

            throw new EntityExistsException(
                    GENRE_SERIES, Map.of("genre", genreSeriesUpsert.genre()
            ));
        }

        GenreSeriesEntity genreSeries = genreSeriesMapper.genreSeriesUpsertToEntity(genreSeriesUpsert);

        genreSeriesRepository.save(genreSeries);

        log.info("Created genre:series with id {}", genreSeries.getGenreSeriesID());

        return genreSeriesMapper.entityToGenreSeries(genreSeries);
    }

    public List<GenreSeries> getGenres() {
        log.info("Getting all genres:series");

        List<GenreSeriesEntity> genreSeries = genreSeriesRepository.findAll(
                Sort.by("genre").ascending()
        );

        log.info("Found {} genres:series", genreSeries.size());

        return genreSeries.isEmpty() ? Collections.emptyList()
                                     : genreSeries.stream()
                                                  .map(genreSeriesMapper::entityToGenreSeries)
                                                  .toList();
    }

    public GenreSeries getGenre(UUID id) {
        log.info("Getting genre:series with id: {}", id);

        return genreSeriesMapper.entityToGenreSeries(
                genreSeriesRepository.findById(id)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             GENRE_SERIES, Map.of("genreSeriesID", id.toString()))
                                     )
        );
    }

    public void update(UUID id, GenreSeriesUpsert genreSeriesUpsert) {
        log.info("Updating genre:series with id: {}", id);

        Optional<GenreSeriesEntity> existingGenreSeries = genreSeriesRepository.findById(id);

        if (existingGenreSeries.isEmpty()) {
            log.error("Genre:series with id {} not found", id);

            throw new EntityNotFoundException(GENRE_SERIES, Map.of("genreSeriesID", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingGenreSeries.get().checkUpdateDtoEqualsEntity(genreSeriesUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            GenreSeriesEntity genreSeries = genreSeriesMapper.genreSeriesEntityUpdateMerge(
                    existingGenreSeries.get(),
                    genreSeriesUpsert
            );

            genreSeriesRepository.save(genreSeries);

            log.info("Updated genre:series with id {}", id);
        }
    }

    @Transactional
    @Modifying
    public void delete(UUID genreSeriesID) {
        log.info("Check if genre series for id: {} exists", genreSeriesID);
        Optional<GenreSeriesEntity> genreSeries = genreSeriesRepository.findById(genreSeriesID);

        if (genreSeries.isEmpty()) {
            log.error("Genre series with id {} not found", genreSeriesID);
            throw new EntityNotFoundException(
                    GENRE_SERIES,
                    Map.of("genreSeriesID", genreSeriesID.toString())
            );
        }

        log.info("Checking if genre series belongs to any series");
        boolean isLinkedToSeries = seriesGenreRepository.existsByGenreSeriesID(genreSeriesID);

        if (isLinkedToSeries) {
            log.info("Genre belongs to at least one series, fetching series to check for deletion criteria");

            List<SeriesEntity> series = seriesGenreRepository.fetchDistinctSeriesForGenre(genreSeriesID);

            boolean allHaveMultipleGenres = series.stream()
                                                  .allMatch(item -> item.getSeriesGenres().size() > 1);

            if (allHaveMultipleGenres) {
                log.info("All series have multiple genres, can delete genre series");

                series.forEach(item -> item.removeSeriesGenre(genreSeries.get()));
                genreSeriesRepository.delete(genreSeries.get());

                log.info("Deleted genre series with id {}", genreSeriesID);
            } else {
                throw new GenericBadRequestException("A series must have at least one genre");
            }
        } else {
            log.info("Genre series id: {} - does not belong to any series, deleting", genreSeriesID);

            genreSeriesRepository.delete(genreSeries.get());

            log.info("Deleted genre series with id {}", genreSeriesID);
        }
    }
}
