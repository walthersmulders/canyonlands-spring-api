package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.GenreMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreMovieRepository extends JpaRepository<GenreMovieEntity, UUID> {
}
