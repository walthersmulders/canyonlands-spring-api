package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.genre.music.GenreMusic;
import com.walthersmulders.mapstruct.dto.genre.music.GenreMusicUpsert;
import com.walthersmulders.mapstruct.mapper.GenreMusicMapper;
import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import com.walthersmulders.persistence.repository.genre.GenreMusicRepository;
import com.walthersmulders.persistence.repository.music.AlbumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GenreMusicService {
    private static final String GENRE_MUSIC = "Genre Music";

    private final GenreMusicRepository genreMusicRepository;
    private final GenreMusicMapper     genreMusicMapper;
    private final AlbumRepository      albumRepository;

    public GenreMusicService(
            GenreMusicRepository genreMusicRepository,
            GenreMusicMapper genreMusicMapper,
            AlbumRepository albumRepository
    ) {
        this.genreMusicRepository = genreMusicRepository;
        this.genreMusicMapper = genreMusicMapper;
        this.albumRepository = albumRepository;
    }

    public GenreMusic create(GenreMusicUpsert genreMusicUpsert) {
        log.info("Creating genre");

        boolean exists = genreMusicRepository.exists(genreMusicUpsert.genre());

        if (exists) {
            log.error("Genre with genre {} already exists", genreMusicUpsert.genre());

            throw new EntityExistsException(
                    GENRE_MUSIC,
                    Map.of("genre", genreMusicUpsert.genre())
            );
        }

        GenreMusicEntity musicGenre = genreMusicMapper.genreMusicUpsertToEntity(genreMusicUpsert);

        genreMusicRepository.save(musicGenre);

        log.info("Created genre with id: {}", musicGenre.getGenreMusicID());

        return genreMusicMapper.entityToGenreMusic(musicGenre);
    }

    public List<GenreMusic> getGenres() {
        log.info("Getting all genre music items");

        List<GenreMusicEntity> musicGenres = genreMusicRepository.findAll();

        log.info("Found {} genre music items", musicGenres.size());

        return musicGenres.isEmpty() ? Collections.emptyList()
                                     : musicGenres.stream()
                                                  .map(genreMusicMapper::entityToGenreMusic)
                                                  .toList();
    }

    public GenreMusic getGenre(UUID id) {
        log.info("Getting genre with id: {}", id);

        return genreMusicMapper.entityToGenreMusic(
                genreMusicRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            GENRE_MUSIC, Map.of("genreMusicID", id.toString()))
                                    )
        );
    }

    public void update(UUID id, GenreMusicUpsert genreMusicUpsert) {
        log.info("Updating genre with id: {}", id);

        Optional<GenreMusicEntity> existingMusicGenre = genreMusicRepository.findById(id);

        if (existingMusicGenre.isEmpty()) {
            log.error("Genre with id {} not found", id);

            throw new EntityNotFoundException(GENRE_MUSIC, Map.of("genreMusicID", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingMusicGenre.get().checkUpdateDtoEqualsEntity(genreMusicUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            GenreMusicEntity updatedMusicGenre = genreMusicMapper.genreMusicEntityUpdateMerge(
                    existingMusicGenre.get(),
                    genreMusicUpsert
            );

            genreMusicRepository.save(updatedMusicGenre);

            log.info("Updated genre with id: {}", id);
        }
    }

    public void delete(UUID id) {
        log.info("Checking if genre belongs to any album");
        boolean isLinkedToAlbum = albumRepository.existsByGenreMusicID(id);

        if (isLinkedToAlbum) {
            log.error("Genre with id {} is linked to an album, cannot delete", id);

            throw new GenericBadRequestException(
                    "Genre with id " + id + " is linked to an album, cannot delete"
            );
        }

        log.info("Deleting genre with id: {}", id);

        genreMusicRepository.deleteById(id);

        log.info("Deleted genre with id: {}", id);
    }
}
