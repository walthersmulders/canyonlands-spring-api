package com.walthersmulders.service.users;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.users.series.UsersSeries;
import com.walthersmulders.mapstruct.dto.users.series.UsersSeriesUpsert;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.mapstruct.mapper.UsersSeriesMapper;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesEntity;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesID;
import com.walthersmulders.persistence.repository.series.SeriesRepository;
import com.walthersmulders.persistence.repository.user.UserRepository;
import com.walthersmulders.persistence.repository.users.UsersSeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UsersSeriesService {
    private static final String SERIES_ID = "seriesID";
    private static final String SERIES    = "Series";
    private static final String USER_ID   = "userID";

    private final UserRepository        userRepository;
    private final UserMapper            userMapper;
    private final SeriesRepository      seriesRepository;
    private final UsersSeriesRepository usersSeriesRepository;
    private final UsersSeriesMapper     usersSeriesMapper;

    public UsersSeriesService(
            UserRepository userRepository,
            UserMapper userMapper,
            SeriesRepository seriesRepository,
            UsersSeriesRepository usersSeriesRepository,
            UsersSeriesMapper usersSeriesMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.seriesRepository = seriesRepository;
        this.usersSeriesRepository = usersSeriesRepository;
        this.usersSeriesMapper = usersSeriesMapper;
    }


    @Transactional
    public UsersSeries addSeriesToUserLibrary(UUID userID, UUID seriesID, UsersSeriesUpsert usersSeriesUpsert) {
        log.info("Adding series to user library for userID: {} and seriesID: {}", userID, seriesID);

        Optional<UserEntity> userWithSeries = userRepository.fetchWithSeries(userID);

        if (userWithSeries.isEmpty()) {
            log.error("User with userID: {} not found", userID);

            throw new EntityNotFoundException("User", Map.of(USER_ID, userID.toString()));
        }

        UsersSeriesEntity existingSeries = userWithSeries.get()
                                                         .getSeries()
                                                         .stream()
                                                         .filter(series -> series.getSeries().getSeriesID().equals(seriesID))
                                                         .findFirst()
                                                         .orElse(null);

        if (existingSeries != null) {
            log.error("Series with seriesID: {} already exists in users library", seriesID);

            throw new EntityExistsException(SERIES, Map.of(SERIES_ID, seriesID.toString()));
        }

        log.info("Series with seriesID: {} not found in user's library. Adding series to library", seriesID);

        SeriesEntity series = seriesRepository.findById(seriesID)
                                              .orElseThrow(() -> new EntityNotFoundException(
                                                      SERIES,
                                                      Map.of(SERIES_ID, seriesID.toString())
                                              ));

        if (usersSeriesUpsert.review() != null) {
            userWithSeries.get().addSeriesToUserLibrary(
                    series,
                    usersSeriesUpsert.rating(),
                    usersSeriesUpsert.review()
            );

            log.info("Series with seriesID: {} added to user's library with review", seriesID);

            return userMapper.entityToUsersSeries(series, usersSeriesUpsert.rating(), usersSeriesUpsert.review());
        } else {
            userWithSeries.get().addSeriesToUserLibrary(series, usersSeriesUpsert.rating());
            log.info("Series with seriesID: {} added to user's library with rating", seriesID);

            return userMapper.entityToUsersSeries(series, usersSeriesUpsert.rating());
        }
    }

    @Transactional(readOnly = true)
    public UsersSeries getUsersSeries(UUID userID, UUID seriesID) {
        log.info("Fetching series with seriesID: {} for userID: {}", seriesID, userID);

        Optional<UsersSeriesEntity> usersSeries = usersSeriesRepository.fetchUsersSeries(
                userID,
                seriesID
        );

        if (usersSeries.isEmpty()) {
            log.error("Combination with userID: {} and seriesID: {} not found", userID, seriesID);

            throw new EntityNotFoundException("UsersSeries", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(SERIES_ID, seriesID.toString())
            ));
        }

        log.info("Combination with userID: {} and seriesID: {} found", userID, seriesID);

        return usersSeriesMapper.entityToUsersSeries(usersSeries.get());
    }

    public List<UsersSeries> getUserSeries(UUID userID) {
        log.info("Fetching all series for user with userID: {}", userID);

        List<UsersSeriesEntity> usersSeries = usersSeriesRepository.fetchAllUsersSeries(userID);

        log.info("Fetched all series for user with userID: {}", userID);

        return usersSeries.stream().map(usersSeriesMapper::entityToUsersSeries).toList();
    }

    public void removeSeriesFromUserLibrary(UUID userID, UUID seriesID) {
        log.info("Removing series with seriesID: {} from user with userID: {}", seriesID, userID);

        usersSeriesRepository.deleteById(new UsersSeriesID(userID, seriesID));

        log.info("Series with seriesID: {} removes from user with userID: {}", seriesID, userID);
    }

    public void update(UUID userID, UUID seriesID, UsersSeriesUpsert usersSeriesUpsert) {
        log.info("Updating series with seriesID: {} for userID: {}", seriesID, userID);

        Optional<UsersSeriesEntity> existingUsersSeries = usersSeriesRepository.findById(
                new UsersSeriesID(userID, seriesID)
        );

        if (existingUsersSeries.isEmpty()) {
            log.error("Combination with userID: {} and seriesID: {} not found", userID, seriesID);

            throw new EntityNotFoundException("UsersSeries", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(SERIES_ID, seriesID.toString())
            ));
        }

        existingUsersSeries.get().setReview(usersSeriesUpsert.review());
        existingUsersSeries.get().setRating(usersSeriesUpsert.rating());

        usersSeriesRepository.save(existingUsersSeries.get());

        log.info("Updated series with seriesID: {} for userID: {}", seriesID, userID);
    }
}
