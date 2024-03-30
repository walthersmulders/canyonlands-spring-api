package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.BookGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenreEntity, UUID> {

    @Query(value = "SELECT CASE WHEN EXISTS " +
                   "(SELECT bge FROM BookGenreEntity bge WHERE bge.genre = :genre AND bge.subGenre = :subGenre) " +
                   "THEN TRUE ELSE FALSE END FROM BookGenreEntity")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "subGenre") String subGenre
    );
}
