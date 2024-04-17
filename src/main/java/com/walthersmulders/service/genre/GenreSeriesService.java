package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeriesUpsert;
import com.walthersmulders.mapstruct.mapper.GenreSeriesMapper;
import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import com.walthersmulders.persistence.repository.genre.GenreSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class GenreSeriesService {
    private static final String GENRE_SERIES = "Genre:Series";

    private final GenreSeriesRepository genreSeriesRepository;
    private final GenreSeriesMapper     genreSeriesMapper;

    public GenreSeriesService(
            GenreSeriesRepository genreSeriesRepository,
            GenreSeriesMapper genreSeriesMapper
    ) {
        this.genreSeriesRepository = genreSeriesRepository;
        this.genreSeriesMapper = genreSeriesMapper;
    }

    public GenreSeries create(GenreSeriesUpsert genreSeriesUpsert) {
        log.info("Creating genre:series");

        boolean exists = genreSeriesRepository.exists(
                genreSeriesUpsert.genre(),
                genreSeriesUpsert.externalID()
        );

        if (exists) {
            log.error(
                    "Genre:series with genre {} already exists",
                    genreSeriesUpsert.genre()
            );

            throw new EntityExistsException(GENRE_SERIES, Map.ofEntries(
                    entry("genre", genreSeriesUpsert.genre()),
                    entry("externalID", String.valueOf(genreSeriesUpsert.externalID()))
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
}
