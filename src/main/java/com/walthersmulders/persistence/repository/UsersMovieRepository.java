package com.walthersmulders.persistence.repository;

import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersMovieRepository extends JpaRepository<UsersMovieEntity, UsersMovieID> {
    @Query("SELECT um " +
           "FROM UsersMovieEntity um " +
           "JOIN FETCH um.movie m " +
           "WHERE um.usersMovieID.userID = :userID " +
           "AND m.movieID = :movieID")
    Optional<UsersMovieEntity> fetchUsersMovie(
            @Param(value = "userID") UUID userID,
            @Param(value = "movieID") UUID movieID
    );

    @Query("SELECT um " +
           "FROM UsersMovieEntity um " +
           "JOIN FETCH um.movie m " +
           "WHERE um.usersMovieID.userID = :userID")
    List<UsersMovieEntity> fetchAllUsersMovies(
            @Param(value = "userID") UUID userID
    );
}
