package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.users.movie.UsersMovie;
import com.walthersmulders.mapstruct.dto.users.movie.UsersMovieUpsert;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.mapstruct.mapper.UsersMovieMapper;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieID;
import com.walthersmulders.persistence.repository.MovieRepository;
import com.walthersmulders.persistence.repository.UserRepository;
import com.walthersmulders.persistence.repository.UsersMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UsersMovieService {
    private static final String MOVIE_ID = "movieID";
    private static final String MOVIE    = "Movie";
    private static final String USER_ID  = "userID";

    private final UserRepository       userRepository;
    private final UserMapper           userMapper;
    private final MovieRepository      movieRepository;
    private final UsersMovieRepository usersMovieRepository;
    private final UsersMovieMapper     usersMovieMapper;

    public UsersMovieService(
            UserRepository userRepository,
            UserMapper userMapper,
            MovieRepository movieRepository,
            UsersMovieRepository usersMovieRepository,
            UsersMovieMapper usersMovieMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.movieRepository = movieRepository;
        this.usersMovieRepository = usersMovieRepository;
        this.usersMovieMapper = usersMovieMapper;
    }

    @Transactional
    public UsersMovie addMovieToUserLibrary(UUID userID, UUID movieID, UsersMovieUpsert usersMovieUpsert) {
        log.info("Adding movie to user library for userID: {} and movieID: {}", userID, movieID);

        Optional<UserEntity> userWithMovies = userRepository.fetchWithMovies(userID);

        if (userWithMovies.isEmpty()) {
            log.error("User with userID: {} not found", userID);

            throw new EntityNotFoundException("User", Map.of(USER_ID, userID.toString()));
        }

        UsersMovieEntity existingMovie = userWithMovies.get()
                                                       .getMovies()
                                                       .stream()
                                                       .filter(movie -> movie.getMovie().getMovieID().equals(movieID))
                                                       .findFirst()
                                                       .orElse(null);

        if (existingMovie != null) {
            log.error("Movie with movieID: {} already exists in user's library", movieID);

            throw new EntityExistsException(MOVIE, Map.of(MOVIE_ID, movieID.toString()));
        }

        log.info("Movie with movieID: {} not found in user's library. Adding movie to library", movieID);

        MovieEntity movie = movieRepository.findById(movieID)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   MOVIE,
                                                   Map.of(MOVIE_ID, movieID.toString())
                                           ));

        if (usersMovieUpsert.review() != null) {
            userWithMovies.get().addMovieToUserLibrary(
                    movie,
                    usersMovieUpsert.rating(),
                    usersMovieUpsert.review()
            );

            log.info("Movie with movieID: {} added to user's library with review", movieID);

            return userMapper.entityToUsersMovie(movie, usersMovieUpsert.rating(), usersMovieUpsert.review());
        } else {
            userWithMovies.get().addMovieToUserLibrary(movie, usersMovieUpsert.rating());
            log.info("Movie with movieID: {} added to user's library with rating", movieID);

            return userMapper.entityToUsersMovie(movie, usersMovieUpsert.rating());
        }
    }

    @Transactional(readOnly = true)
    public UsersMovie getUsersMovie(UUID userID, UUID movieID) {
        log.info("Fetching movie with movieID: {} for userID: {}", movieID, userID);

        Optional<UsersMovieEntity> usersMovie = usersMovieRepository.fetchUsersMovie(userID, movieID);

        if (usersMovie.isEmpty()) {
            log.error("Combination with userID: {} and movieID: {} not found", userID, movieID);

            throw new EntityNotFoundException("UsersBook", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(MOVIE_ID, movieID.toString())
            ));
        }

        log.info("Combination with userID: {} and movieID: {} found", userID, movieID);

        return usersMovieMapper.entityToUsersMovie(usersMovie.get());
    }

    public List<UsersMovie> getUserMovies(UUID userID) {
        log.info("Fetching all movies for user with userID: {}", userID);

        List<UsersMovieEntity> usersMovies = usersMovieRepository.fetchAllUsersMovies(userID);

        log.info("Fetched all movies for user with userID: {}", userID);

        return usersMovies.stream().map(usersMovieMapper::entityToUsersMovie).toList();
    }

    public void removeMovieFromUserLibrary(UUID userID, UUID movieID) {
        log.info("Removing movie with movieID: {} from user with userID: {}", movieID, userID);

        usersMovieRepository.deleteById(new UsersMovieID(userID, movieID));

        log.info("Movie with movieID: {} removed from user with userID: {}", movieID, userID);
    }

    public void update(UUID userID, UUID movieID, UsersMovieUpsert usersMovieUpsert) {
        log.info("Updating movie with movieID: {} for user with userID: {}", movieID, userID);

        Optional<UsersMovieEntity> existingUsersMovie = usersMovieRepository.findById(
                new UsersMovieID(userID, movieID)
        );

        if (existingUsersMovie.isEmpty()) {
            log.error("Combination with userID: {} and movieID: {} not found", userID, movieID);

            throw new EntityNotFoundException("UsersMovie", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(MOVIE_ID, movieID.toString())
            ));
        }

        existingUsersMovie.get().setReview(usersMovieUpsert.review());
        existingUsersMovie.get().setRating(usersMovieUpsert.rating());

        usersMovieRepository.save(existingUsersMovie.get());

        log.info("Updated movie with movieID: {} for user with userID: {}", movieID, userID);
    }
}
