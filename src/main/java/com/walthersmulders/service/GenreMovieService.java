package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genremovie.GenreMovieUpsert;
import com.walthersmulders.mapstruct.mapper.GenreMovieMapper;
import com.walthersmulders.persistance.entity.GenreMovieEntity;
import com.walthersmulders.persistance.repository.GenreMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Service
@Slf4j
public class GenreMovieService {
    private final static String GENRE_MOVIE = "Genre:Movie";

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
}
