package com.walthersmulders.persistence.repository.users;

import com.walthersmulders.persistence.entity.users.music.UsersAlbumEntity;
import com.walthersmulders.persistence.entity.users.music.UsersMusicID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersMusicRepository extends JpaRepository<UsersAlbumEntity, UsersMusicID> {
    @Query("SELECT um " +
           "FROM UsersAlbumEntity um " +
           "JOIN FETCH um.album a " +
           "WHERE um.usersMusicID.userID = :userID " +
           "AND a.albumID = :albumID")
    Optional<UsersAlbumEntity> fetchUsersAlbum(
            @Param(value = "userID") UUID userID,
            @Param(value = "albumID") UUID albumID
    );

    @Query("SELECT um " +
           "FROM UsersAlbumEntity um " +
           "JOIN FETCH um.album a " +
           "WHERE um.usersMusicID.userID = :userID")
    List<UsersAlbumEntity> fetchAllUsersAlbums(
            @Param(value = "userID") UUID userID
    );
}
