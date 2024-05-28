package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovieUpsert;
import com.walthersmulders.mapstruct.mapper.GenreMovieMapper;
import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.repository.genre.GenreMovieRepository;
import com.walthersmulders.persistence.repository.movie.MovieGenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class GenreMovieService {
    private static final String GENRE_MOVIE = "Genre:Movie";

    private final GenreMovieRepository genreMovieRepository;
    private final GenreMovieMapper     genreMovieMapper;
    private final MovieGenreRepository movieGenreRepository;

    public GenreMovieService(
            GenreMovieRepository genreMovieRepository,
            GenreMovieMapper genreMovieMapper,
            MovieGenreRepository movieGenreRepository
    ) {
        this.genreMovieRepository = genreMovieRepository;
        this.genreMovieMapper = genreMovieMapper;
        this.movieGenreRepository = movieGenreRepository;
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
                genreMovieUpsert.genre()
        );

        if (exists) {
            log.error(
                    "Genre:movie with genre {} already exists",
                    genreMovieUpsert.genre()
            );

            throw new EntityExistsException(
                    GENRE_MOVIE, Map.of("genre", genreMovieUpsert.genre()
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

    @Transactional
    @Modifying
    public void delete(UUID genreMovieID) {
        log.info("Check if genre movie for id: {} exists", genreMovieID);
        Optional<GenreMovieEntity> genreMovie = genreMovieRepository.findById(genreMovieID);

        if (genreMovie.isEmpty()) {
            log.error("Genre:movie with id {} not found", genreMovieID);
            throw new EntityNotFoundException(
                    GENRE_MOVIE,
                    Map.of("genreMovieId", genreMovieID.toString())
            );
        }

        log.info("Checking if genre movie belongs to any movie");
        boolean isLinkedToMovie = movieGenreRepository.existsByGenreMovieID(genreMovieID);

        if (isLinkedToMovie) {
            log.info("Genre belongs to at least one movie, fetching movies to check for deletion criteria");

            List<MovieEntity> movies = movieGenreRepository.fetchDistinctMoviesForGenre(genreMovieID);

            boolean allHaveMultipleGenres = movies.stream()
                                                  .allMatch(item -> item.getMovieGenres().size() > 1);

            if (allHaveMultipleGenres) {
                log.info("All movies have multiple genres, can delete genre movie");

                movies.forEach(item -> item.removeMovieGenre(genreMovie.get()));
                genreMovieRepository.delete(genreMovie.get());

                log.info("Deleted genre movie with id {}", genreMovieID);
            } else {
                throw new GenericBadRequestException("A movie must have at least one genre");
            }
        } else {
            log.info("Genre movie id: {} - does not belong to any movie, deleting", genreMovieID);

            genreMovieRepository.delete(genreMovie.get());

            log.info("Deleted genre movie with id: {}", genreMovieID);
        }
    }
}
