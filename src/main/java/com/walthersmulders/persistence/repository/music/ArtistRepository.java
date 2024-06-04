package com.walthersmulders.persistence.repository.music;

import com.walthersmulders.persistence.entity.music.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM ArtistEntity a " +
           "WHERE a.firstName = :firstName " +
           "AND a.lastName = :lastName")
    boolean exists(
            @Param(value = "firstName") String firstName,
            @Param(value = "lastName") String lastName
    );

    @Query("SELECT a " +
           "FROM ArtistEntity a " +
           "LEFT JOIN FETCH a.albums")
    List<ArtistEntity> fetchAllWithAlbums();

    @Query("SELECT a " +
           "FROM ArtistEntity a " +
           "LEFT JOIN FETCH a.albums " +
           "WHERE a.artistID = :artistID")
    Optional<ArtistEntity> fetchArtistWithAlbums(
            @Param(value = "artistID") UUID artistID
    );
}
