package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.genre.GenreTvSeries;
import com.walthersmulders.mapstruct.dto.genre.GenreTvSeriesUpsert;
import com.walthersmulders.mapstruct.mapper.GenreTvSeriesMapper;
import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import com.walthersmulders.persistence.repository.genre.GenreTvSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class GenreTvSeriesService {
    private static final String GENRE_TV_SERIES = "Genre:TV-Series";

    private final GenreTvSeriesRepository genreTvSeriesRepository;
    private final GenreTvSeriesMapper     genreTvSeriesMapper;

    public GenreTvSeriesService(
            GenreTvSeriesRepository genreTvSeriesRepository,
            GenreTvSeriesMapper genreTvSeriesMapper
    ) {
        this.genreTvSeriesRepository = genreTvSeriesRepository;
        this.genreTvSeriesMapper = genreTvSeriesMapper;
    }

    public GenreTvSeries create(GenreTvSeriesUpsert genreTvSeriesUpsert) {
        log.info("Creating genre:tv-series");

        boolean exists = genreTvSeriesRepository.exists(
                genreTvSeriesUpsert.genre(),
                genreTvSeriesUpsert.externalID()
        );

        if (exists) {
            log.error(
                    "Genre:tv-series with genre {} already exists",
                    genreTvSeriesUpsert.genre()
            );

            throw new EntityExistsException(GENRE_TV_SERIES, Map.ofEntries(
                    entry("genre", genreTvSeriesUpsert.genre()),
                    entry("externalID", String.valueOf(genreTvSeriesUpsert.externalID()))
            ));
        }

        GenreTvSeriesEntity genreTvSeries = genreTvSeriesMapper.genreTvSeriesUpsertToEntity(genreTvSeriesUpsert);

        genreTvSeriesRepository.save(genreTvSeries);

        log.info("Created genre:tv-series with id {}", genreTvSeries.getGenreTvSeriesID());

        return genreTvSeriesMapper.entityToGenreTvSeries(genreTvSeries);
    }

    public List<GenreTvSeries> getGenres() {
        log.info("Getting all genres:tv-series");

        List<GenreTvSeriesEntity> genreTvSeries = genreTvSeriesRepository.findAll(
                Sort.by("genre").ascending()
        );

        log.info("Found {} genres:tv-series", genreTvSeries.size());

        return genreTvSeries.isEmpty() ? Collections.emptyList()
                                       : genreTvSeries.stream()
                                                      .map(genreTvSeriesMapper::entityToGenreTvSeries)
                                                      .toList();
    }

    public GenreTvSeries getGenre(UUID id) {
        log.info("Getting genre:tv-series with id: {}", id);

        return genreTvSeriesMapper.entityToGenreTvSeries(
                genreTvSeriesRepository.findById(id)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               GENRE_TV_SERIES, Map.of("genreTvSeriesId", id.toString()))
                                       )
        );
    }

    public void update(UUID id, GenreTvSeriesUpsert genreTvSeriesUpsert) {
        log.info("Updating genre:tv-series with id: {}", id);

        Optional<GenreTvSeriesEntity> existingGenreTvSeries = genreTvSeriesRepository.findById(id);

        if (existingGenreTvSeries.isEmpty()) {
            log.error("Genre:tv-series with id {} not found", id);

            throw new EntityNotFoundException(GENRE_TV_SERIES, Map.of("genreTvSeriesId", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingGenreTvSeries.get().checkUpdateDtoEqualsEntity(genreTvSeriesUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            GenreTvSeriesEntity genreTvSeries = genreTvSeriesMapper.genreTvSeriesEntityUpdateMerge(
                    existingGenreTvSeries.get(),
                    genreTvSeriesUpsert
            );

            genreTvSeriesRepository.save(genreTvSeries);

            log.info("Updated genre:tv-series with id {}", id);
        }
    }
}
