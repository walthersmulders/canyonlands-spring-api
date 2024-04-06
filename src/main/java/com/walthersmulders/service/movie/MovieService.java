package com.walthersmulders.service.movie;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.movie.Movie;
import com.walthersmulders.mapstruct.dto.movie.MovieUpsert;
import com.walthersmulders.mapstruct.dto.movie.MovieWithLinks;
import com.walthersmulders.mapstruct.dto.movie.MovieWithLinksUpsert;
import com.walthersmulders.mapstruct.mapper.MovieMapper;
import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.movie.MovieGenreEntity;
import com.walthersmulders.persistence.repository.genre.GenreMovieRepository;
import com.walthersmulders.persistence.repository.movie.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MovieService {
    private static final String MOVIE    = "MOVIE";
    private static final String MOVIE_ID = "MOVIE_ID";

    private final MovieRepository      movieRepository;
    private final MovieMapper          movieMapper;
    private final GenreMovieRepository genreMovieRepository;

    public MovieService(
            MovieRepository movieRepository,
            MovieMapper movieMapper,
            GenreMovieRepository genreMovieRepository
    ) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreMovieRepository = genreMovieRepository;
    }

    @Transactional
    public MovieWithLinks createMovieWithLinks(MovieWithLinksUpsert movieWithLinksUpsert) {
        log.info("Creating movie with links");
        log.info("Check if movie with title already exists");

        boolean existsByTitle = movieRepository.existsByTitle(movieWithLinksUpsert.movie().title());

        log.info("Check if movie with externalID already exists");

        boolean existsByExternalID = movieRepository.existsByExternalID(
                movieWithLinksUpsert.movie().externalID()
        );

        Map<String, String> errorsMap = new HashMap<>();

        if (existsByTitle || existsByExternalID) {
            if (existsByTitle) {
                errorsMap.put("title", "Movie with this title already exists");
            }

            if (existsByExternalID) {
                errorsMap.put("externalID", "Movie with this externalID already exists");
            }

            throw new EntityExistsException(MOVIE, errorsMap);
        }

        // Will throw error if any of the IDs supplied are invalid.
        List<GenreMovieEntity> genreMovies = genreMovieRepository.findAllById(
                movieWithLinksUpsert.genreIDs()
        );

        MovieEntity movie = movieMapper.movieUpsertToEntity(movieWithLinksUpsert.movie());

        movie.setDateAdded(LocalDateTime.now());
        movie.setDateUpdated(LocalDateTime.now());

        genreMovies.forEach(movie::addMovieGenre);

        if (movie.getMovieGenres().isEmpty()) {
            log.error("Movie must have at least one genre");

            throw new GenericBadRequestException("Movie must have at least one genre");
        }

        movieRepository.save(movie);

        return movieMapper.entityToMovieWithLinks(movie);
    }

    public List<Movie> getMovies() {
        log.info("Getting all movies");

        List<MovieEntity> movies = movieRepository.findAll();

        log.info("Found {} movies", movies.size());

        return movies.isEmpty() ? Collections.emptyList()
                                : movies.stream()
                                        .map(movieMapper::entityToMovie)
                                        .toList();
    }

    @Transactional(readOnly = true)
    public List<MovieWithLinks> getMoviesWithLinks() {
        log.info("Getting all movies with links");

        List<MovieEntity> moviesWithLinks = movieRepository.fetchMoviesWithLinks();

        log.info("Found {} movies with links", moviesWithLinks.size());

        return moviesWithLinks.isEmpty() ? Collections.emptyList()
                                         : moviesWithLinks.stream()
                                                          .map(movieMapper::entityToMovieWithLinks)
                                                          .toList();

    }

    public Movie getMovie(UUID id) {
        log.info("Getting movie with id: {}", id);

        MovieEntity movie = movieRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   MOVIE,
                                                   Map.of(MOVIE_ID, id.toString())
                                           ));

        return movieMapper.entityToMovie(movie);
    }

    @Transactional(readOnly = true)
    public MovieWithLinks getMovieWithLinks(UUID id) {
        log.info("Getting movie with links with id: {}", id);

        MovieEntity movieWithLinks = movieRepository.fetchMovieWithLinks(id)
                                                    .orElseThrow(() -> new EntityNotFoundException(
                                                            MOVIE,
                                                            Map.of(MOVIE_ID, id.toString())
                                                    ));

        return movieMapper.entityToMovieWithLinks(movieWithLinks);
    }

    public void updateMovie(UUID id, MovieUpsert movieUpsert) {
        log.info("Updating movie with id: {}", id);

        Optional<MovieEntity> existingMovie = movieRepository.findById(id);

        if (existingMovie.isEmpty()) {
            log.error("Movie with movieID {} not found", id);

            throw new EntityNotFoundException(
                    MOVIE,
                    Map.of(MOVIE_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingMovie.get().checkUpdateDtoEqualsEntity(movieUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields, updating movie");

            if (!existingMovie.get().getTitle().equals(movieUpsert.title())) {
                log.info("Check if movie with title already exists");

                boolean existsByTitle = movieRepository.existsByTitle(movieUpsert.title());

                if (existsByTitle) {
                    throw new EntityExistsException(
                            MOVIE,
                            Map.of("title", "Movie with this title already exists")
                    );
                }
            }

            if (!existingMovie.get().getExternalID().equals(movieUpsert.externalID())) {
                log.info("Check if movie with externalID already exists");

                boolean existsByExternalID = movieRepository.existsByExternalID(movieUpsert.externalID());

                if (existsByExternalID) {
                    throw new EntityExistsException(
                            MOVIE,
                            Map.of("externalID", "Movie with this externalID already exists")
                    );
                }
            }

            MovieEntity updatedMovie = movieMapper.movieEntityUpdateMerge(
                    existingMovie.get(),
                    movieUpsert
            );

            updatedMovie.setDateUpdated(LocalDateTime.now());

            movieRepository.save(updatedMovie);

            log.info("Movie with id {} updated", id);
        }
    }

    @Transactional
    public void addGenre(UUID movieID, UUID genreID) {
        log.info("Adding genre with id {} to movie with id {}", genreID, movieID);

        Optional<MovieEntity> movie = movieRepository.fetchMovieWithLinks(movieID);

        if (movie.isEmpty()) {
            log.error("Movie with movieID {} not found", movieID);

            throw new EntityNotFoundException(
                    MOVIE,
                    Map.of(MOVIE_ID, movieID.toString())
            );
        }

        MovieGenreEntity movieGenre = movie.get().getMovieGenres()
                                           .stream()
                                           .filter(genre -> genre.getGenreMovie().getGenreMovieID().equals(genreID))
                                           .findFirst()
                                           .orElse(null);

        if (movieGenre != null) {
            log.error("Movie with movieID {} already has genre with genreID {}", movieID, genreID);

            throw new EntityExistsException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        Optional<GenreMovieEntity> genreMovie = genreMovieRepository.findById(genreID);

        if (genreMovie.isEmpty()) {
            log.error("Genre with genreID {} not found", genreID);

            throw new EntityNotFoundException(
                    "GENRE",
                    Map.of("GENRE_ID", genreID.toString())
            );
        }

        movie.get().addMovieGenre(genreMovie.get());

        movieRepository.save(movie.get());

        log.info("Genre with id {} added to movie with id {}", genreID, movieID);
    }

    @Transactional
    public void removeGenreMovie(UUID movieID, UUID genreID) {
        log.info("Removing genre with id {} from movie with id {}", genreID, movieID);

        Optional<MovieEntity> movie = movieRepository.fetchMovieWithLinks(movieID);

        if (movie.isEmpty()) {
            log.error("Movie with movieID {} not found", movieID);

            throw new EntityNotFoundException(
                    MOVIE,
                    Map.of(MOVIE_ID, movieID.toString())
            );
        }

        MovieGenreEntity movieGenre = movie.get().getMovieGenres()
                                           .stream()
                                           .filter(genre -> genre.getGenreMovie().getGenreMovieID().equals(genreID))
                                           .findFirst()
                                           .orElse(null);

        if (movieGenre != null) {
            if (movieGenre.getMovie().getMovieGenres().size() <= 1) {
                log.error("A movie must have at least one genre");

                throw new GenericBadRequestException("A movie must have at least one genre");
            }

            movieGenre.getMovie().removeMovieGenre(movieGenre.getGenreMovie());
        }
    }
}
