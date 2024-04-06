package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovieUpsert;
import com.walthersmulders.mapstruct.mapper.GenreMovieMapper;
import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import com.walthersmulders.persistence.repository.GenreMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class GenreMovieService {
    private static final String GENRE_MOVIE = "Genre:Movie";

    private final GenreMovieRepository genreMovieRepository;
    private final GenreMovieMapper     genreMovieMapper;

    public GenreMovieService(
            GenreMovieRepository genreMovieRepository,
            GenreMovieMapper genreMovieMapper
    ) {
        this.genreMovieRepository = genreMovieRepository;
        this.genreMovieMapper = genreMovieMapper;
    }

    public List<GenreMovie> getGenres() {
        log.info("Getting all genres:movie");

        List<GenreMovieEntity> genreMovies = genreMovieRepository.findAll(Sort.by("genre")
                                                                              .ascending()
        );

        log.info("Found {} genres:movie", genreMovies.size());

        return genreMovies.isEmpty() ? Collections.emptyList()
                                     : genreMovies.stream()
                                                  .map(genreMovieMapper::entityToGenreMovie)
                                                  .toList();
    }

    public GenreMovie create(GenreMovieUpsert genreMovieUpsert) {
        log.info("Creating genre:movie");

        boolean exists = genreMovieRepository.exists(
                genreMovieUpsert.genre(),
                genreMovieUpsert.externalID()
        );

        if (exists) {
            log.error(
                    "Genre:movie with genre {} already exists",
                    genreMovieUpsert.genre()
            );

            throw new EntityExistsException(
                    GENRE_MOVIE, Map.ofEntries(
                    entry("genre", genreMovieUpsert.genre()),
                    entry("externalID", String.valueOf(genreMovieUpsert.externalID()))
            ));
        }

        GenreMovieEntity genreMovie = genreMovieMapper.genreMovieUpsertToEntity(genreMovieUpsert);

        genreMovieRepository.save(genreMovie);

        log.info("Created genre:movie with id {}", genreMovie.getGenreMovieID());

        return genreMovieMapper.entityToGenreMovie(genreMovie);

    }

    public GenreMovie getGenre(UUID id) {
        log.info("Getting genre:movie with id: {}", id);

        return genreMovieMapper.entityToGenreMovie(
                genreMovieRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            GENRE_MOVIE, Map.of("genreMovieId", id.toString()))
                                    )
        );
    }

    public void update(UUID id, GenreMovieUpsert genreMovieUpsert) {
        log.info("Updating genre:movie with id: {}", id);

        Optional<GenreMovieEntity> existingGenreMovie = genreMovieRepository.findById(id);

        if (existingGenreMovie.isEmpty()) {
            log.error("Genre:movie with id {} not found", id);

            throw new EntityNotFoundException(GENRE_MOVIE, Map.of("genreMovieId", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingGenreMovie.get().checkUpdateDtoEqualsEntity(genreMovieUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            GenreMovieEntity genreMovie = genreMovieMapper.genreMovieEntityUpdateMerge(
                    existingGenreMovie.get(),
                    genreMovieUpsert
            );

            genreMovieRepository.save(genreMovie);

            log.info("Updated genre:movie with id: {}", id);
        }
    }
}
