package com.walthersmulders.persistence.repository.music;

import com.walthersmulders.persistence.entity.music.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, UUID> {
}
