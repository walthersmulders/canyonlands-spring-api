package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.BookGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenreEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(bge) > 0 THEN true ELSE false END " +
           "FROM BookGenreEntity bge " +
           "WHERE bge.genre = :genre " +
           "AND bge.subGenre = :subGenre")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "subGenre") String subGenre
    );
}
