package com.walthersmulders.persistence.repository.genre;

import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreMusicRepository extends JpaRepository<GenreMusicEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(gme) > 0 THEN true ELSE false END " +
           "FROM GenreMusicEntity gme " +
           "WHERE gme.genre = :genre")
    boolean exists(
            @Param(value = "genre") String genre
    );
}
