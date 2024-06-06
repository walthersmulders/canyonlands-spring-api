package com.walthersmulders.persistence.repository.music;

import com.walthersmulders.persistence.entity.music.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM AlbumEntity a " +
           "WHERE a.genreMusic.genreMusicID = :genreMusicID")
    boolean existsByGenreMusicID(
            @Param(value = "genreMusicID") UUID genreMusicID
    );

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM AlbumEntity a " +
           "WHERE a.title = :title")
    boolean existsByTitle(
            @Param(value = "title") String title
    );

    @Query("SELECT a " +
           "FROM AlbumEntity a " +
           "LEFT JOIN FETCH a.artists")
    List<AlbumEntity> fetchAlbumsWithLinks();

    @Query("SELECT a " +
           "FROM AlbumEntity a " +
           "LEFT JOIN FETCH a.artists " +
           "WHERE a.albumID = :albumID")
    Optional<AlbumEntity> fetchAlbumWithLinks(
            @Param(value = "albumID") UUID albumID
    );

    @Query("SELECT a " +
           "FROM AlbumEntity a " +
           "LEFT JOIN FETCH a.artists " +
           "WHERE a.albumID = :albumID")
    Optional<AlbumEntity> fetchAlbumWithArtists(
            @Param(value = "albumID") UUID albumID
    );
}
