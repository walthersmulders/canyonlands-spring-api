package com.walthersmulders.service.tvseries;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeries;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesUpsert;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinks;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinksUpsert;
import com.walthersmulders.mapstruct.mapper.TvSeriesMapper;
import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import com.walthersmulders.persistence.entity.tvseries.TvSeriesEntity;
import com.walthersmulders.persistence.entity.tvseries.TvSeriesGenreEntity;
import com.walthersmulders.persistence.repository.genre.GenreTvSeriesRepository;
import com.walthersmulders.persistence.repository.tvseries.TvSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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

    @Transactional
    public TvSeriesWithLinks createWithLinks(TvSeriesWithLinksUpsert tvSeriesWithLinksUpsert) {
        log.info("Creating TvSeries with links");
        log.info("Check if tv-series with title already exists");

        boolean existsByTitle = tvSeriesRepository.existsByTitle(tvSeriesWithLinksUpsert.tvSeries().title());

        log.info("Check if tv-series with externalID already exists");

        boolean existsByExternalID = tvSeriesRepository.existsByExternalID(
                tvSeriesWithLinksUpsert.tvSeries().externalID()
        );

        Map<String, String> errorsMap = new HashMap<>();

        if (existsByTitle || existsByExternalID) {
            if (existsByTitle) {
                errorsMap.put("title", "TvSeries with title already exists");
            }

            if (existsByExternalID) {
                errorsMap.put("externalID", "TvSeries with externalID already exists");
            }

            throw new EntityExistsException(TV_SERIES, errorsMap);
        }

        // Will throw error if any of the IDs supplied are invalid.
        List<GenreTvSeriesEntity> genreTvSeries = genreTvSeriesRepository.findAllById(
                tvSeriesWithLinksUpsert.genreIDs()
        );

        TvSeriesEntity tvSeries = tvSeriesMapper.tvSeriesUpsertToEntity(tvSeriesWithLinksUpsert.tvSeries());

        tvSeries.setDateAdded(LocalDateTime.now());
        tvSeries.setDateUpdated(LocalDateTime.now());

        genreTvSeries.forEach(tvSeries::addTvSeriesGenre);

        if (tvSeries.getTvSeriesGenres().isEmpty()) {
            log.error("TvSeries must have at least one genre");

            throw new GenericBadRequestException("TvSeries must have at least one genre");
        }

        tvSeriesRepository.save(tvSeries);

        return tvSeriesMapper.entityToTvSeriesWithLinks(tvSeries);
    }

    public List<TvSeries> getAllTvSeries() {
        log.info("Getting all tv-series");

        List<TvSeriesEntity> tvSeries = tvSeriesRepository.findAll();

        log.info("Found {} tv-series", tvSeries.size());

        return tvSeries.isEmpty() ? Collections.emptyList()
                                  : tvSeries.stream()
                                            .map(tvSeriesMapper::entityToTvSeries)
                                            .toList();
    }

    @Transactional(readOnly = true)
    public List<TvSeriesWithLinks> getAllTvSeriesWithLinks() {
        log.info("Getting all tv-series with links");

        List<TvSeriesEntity> tvSeriesWithLinks = tvSeriesRepository.fetchTvSeriesWithLinks();

        log.info("Found {} tv-series with links", tvSeriesWithLinks.size());

        return tvSeriesWithLinks.isEmpty() ? Collections.emptyList()
                                           : tvSeriesWithLinks.stream()
                                                              .map(tvSeriesMapper::entityToTvSeriesWithLinks)
                                                              .toList();
    }

    public TvSeries getTvSeries(UUID id) {
        log.info("Getting tv-series with id {}", id);

        TvSeriesEntity tvSeries = tvSeriesRepository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException(
                                                            TV_SERIES,
                                                            Map.of(TV_SERIES_ID, id.toString())
                                                    ));

        return tvSeriesMapper.entityToTvSeries(tvSeries);
    }

    @Transactional(readOnly = true)
    public TvSeriesWithLinks getTvSeriesWithLinks(UUID id) {
        log.info("Getting tv-series with links with id: {}", id);

        TvSeriesEntity tvSeries = tvSeriesRepository.fetchTvSeriesWithLinks(id)
                                                    .orElseThrow(() -> new EntityNotFoundException(
                                                            TV_SERIES,
                                                            Map.of(TV_SERIES_ID, id.toString())
                                                    ));

        return tvSeriesMapper.entityToTvSeriesWithLinks(tvSeries);
    }

    public void updateTvSeries(UUID id, TvSeriesUpsert tvSeriesUpsert) {
        log.info("Updating tv-series with id {}", id);

        Optional<TvSeriesEntity> existingTvSeries = tvSeriesRepository.findById(id);

        if (existingTvSeries.isEmpty()) {
            log.error("TvSeries with id {} not found", id);

            throw new EntityNotFoundException(
                    TV_SERIES,
                    Map.of(TV_SERIES_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingTvSeries.get().checkUpdateDtoEqualsEntity(tvSeriesUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields, updating movie");

            if (!existingTvSeries.get().getTitle().equals(tvSeriesUpsert.title())) {
                log.info("Check if tv-series with title already exists");

                boolean existsByTitle = tvSeriesRepository.existsByTitle(tvSeriesUpsert.title());

                if (existsByTitle) {
                    throw new EntityExistsException(
                            TV_SERIES,
                            Map.of(TV_SERIES_ID, id.toString())
                    );
                }
            }

            if (!existingTvSeries.get().getExternalID().equals(tvSeriesUpsert.externalID())) {
                log.info("Check if tv-series with externalID already exists");

                boolean existsByExternalID = tvSeriesRepository.existsByExternalID(tvSeriesUpsert.externalID());

                if (existsByExternalID) {
                    throw new EntityExistsException(
                            TV_SERIES,
                            Map.of("externalID", tvSeriesUpsert.externalID().toString())
                    );
                }
            }

            TvSeriesEntity updatedTvSeries = tvSeriesMapper.tvSeriesEntityUpdateMerge(
                    existingTvSeries.get(),
                    tvSeriesUpsert
            );

            updatedTvSeries.setDateUpdated(LocalDateTime.now());

            tvSeriesRepository.save(updatedTvSeries);

            log.info("TvSeries with id {} has been updated", id);
        }
    }

    @Transactional
    public void addGenre(UUID tvSeriesID, UUID genreID) {
        log.info("Adding genre with id {} to tv-series with id {}", genreID, tvSeriesID);

        Optional<TvSeriesEntity> tvSeries = tvSeriesRepository.fetchTvSeriesWithLinks(tvSeriesID);

        if (tvSeries.isEmpty()) {
            log.error("TvSeries with tvSeriesID {} not found", tvSeriesID);

            throw new EntityNotFoundException(
                    TV_SERIES,
                    Map.of(TV_SERIES_ID, tvSeriesID.toString())
            );
        }

        TvSeriesGenreEntity tvSeriesGenre = tvSeries.get().getTvSeriesGenres()
                                                    .stream()
                                                    .filter(genre -> genre.getGenreTvSeries().getGenreTvSeriesID().equals(genreID))
                                                    .findFirst()
                                                    .orElse(null);

        if (tvSeriesGenre != null) {
            log.error("TvSeries with tvSeriesID {} already has a genre with genreID {}", tvSeriesID, genreID);

            throw new EntityExistsException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        Optional<GenreTvSeriesEntity> genreTvSeries = genreTvSeriesRepository.findById(genreID);

        if (genreTvSeries.isEmpty()) {
            log.error("Genre with genreID {} not found", genreID);

            throw new EntityNotFoundException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        tvSeries.get().addTvSeriesGenre(genreTvSeries.get());

        tvSeriesRepository.save(tvSeries.get());

        log.info("Genre with id {} added to tv-series with id {}", genreID, tvSeriesID);
    }

    @Transactional
    public void removeGenreFromTvSeries(UUID tvSeriesID, UUID genresID) {
        log.info("Removing genre with id {} from tv-series with id {}", genresID, tvSeriesID);

        Optional<TvSeriesEntity> tvSeries = tvSeriesRepository.fetchTvSeriesWithLinks(tvSeriesID);

        if (tvSeries.isEmpty()) {
            log.error("TvSeries with tvSeriesID {} not found", tvSeriesID);

            throw new EntityNotFoundException(
                    TV_SERIES,
                    Map.of(TV_SERIES_ID, tvSeriesID.toString())
            );
        }

        TvSeriesGenreEntity tvSeriesGenre = tvSeries.get().getTvSeriesGenres()
                                                    .stream()
                                                    .filter(genre -> genre.getGenreTvSeries().getGenreTvSeriesID().equals(genresID))
                                                    .findFirst()
                                                    .orElse(null);

        if (tvSeriesGenre != null) {
            if (tvSeriesGenre.getTvSeries().getTvSeriesGenres().size() <= 1) {
                log.error("A TvSeries must have at least one genre");

                throw new GenericBadRequestException("A TvSeries must have at least one genre");
            }

            tvSeriesGenre.getTvSeries().removeTvSeriesGenre(tvSeriesGenre.getGenreTvSeries());
        }
    }
}
