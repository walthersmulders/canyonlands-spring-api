package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.GenreBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreBookRepository extends JpaRepository<GenreBookEntity, UUID> {

    @Query(value = "SELECT CASE WHEN EXISTS " +
                   "(SELECT gb FROM GenreBookEntity gb WHERE gb.genre = :genre AND gb.subGenre = :subGenre) " +
                   "THEN TRUE ELSE FALSE END FROM GenreBookEntity")
    boolean exists(
            @Param(value = "genre") String genre,
            @Param(value = "subGenre") String subGenre
    );
}
