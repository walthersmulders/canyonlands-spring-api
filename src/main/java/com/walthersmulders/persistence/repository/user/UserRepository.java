package com.walthersmulders.persistence.repository.user;

import com.walthersmulders.persistence.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT ue " +
           "FROM UserEntity ue " +
           "LEFT JOIN FETCH ue.books " +
           "WHERE ue.userID = :userID")
    Optional<UserEntity> fetchWithBooks(
            @Param(value = "userID") UUID userID
    );

    @Query("SELECT ue " +
           "FROM UserEntity ue " +
           "LEFT JOIN FETCH ue.movies " +
           "WHERE ue.userID = :userID")
    Optional<UserEntity> fetchWithMovies(
            @Param(value = "userID") UUID userID
    );
}
