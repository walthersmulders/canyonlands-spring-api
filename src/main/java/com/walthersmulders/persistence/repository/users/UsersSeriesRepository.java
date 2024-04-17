package com.walthersmulders.persistence.repository.users;

import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesEntity;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersSeriesRepository extends JpaRepository<UsersSeriesEntity, UsersSeriesID> {
    @Query("SELECT us " +
           "FROM UsersSeriesEntity us " +
           "JOIN FETCH us.series s " +
           "WHERE us.usersSeriesID.userID = :userID " +
           "AND s.seriesID = :seriesID")
    Optional<UsersSeriesEntity> fetchUsersSeries(
            @Param(value = "userID") UUID userID,
            @Param(value = "seriesID") UUID seriesID
    );

    @Query("SELECT us " +
           "FROM UsersSeriesEntity us " +
           "JOIN FETCH us.series s " +
           "WHERE us.usersSeriesID.userID = :userID")
    List<UsersSeriesEntity> fetchAllUsersSeries(
            @Param(value = "userID") UUID userID
    );
}
