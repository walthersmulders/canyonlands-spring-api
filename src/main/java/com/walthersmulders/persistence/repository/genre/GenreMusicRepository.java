package com.walthersmulders.persistence.repository.genre;

import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreMusicRepository extends JpaRepository<GenreMusicEntity, UUID> {
}
