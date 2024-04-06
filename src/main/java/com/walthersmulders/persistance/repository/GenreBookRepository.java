package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.GenreBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreBookRepository extends JpaRepository<GenreBookEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(gbe) > 0 THEN true ELSE false END " +
           "FROM GenreBookEntity gbe " +
           "WHERE gbe.genre = :genre " +
           "AND gbe.subGenre = :subGenre")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "subGenre") String subGenre
    );
}
