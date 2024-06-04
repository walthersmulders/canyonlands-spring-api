package com.walthersmulders.service.music;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import com.walthersmulders.mapstruct.mapper.AlbumMapper;
import com.walthersmulders.mapstruct.mapper.ArtistMapper;
import com.walthersmulders.persistence.entity.music.ArtistEntity;
import com.walthersmulders.persistence.repository.genre.GenreMusicRepository;
import com.walthersmulders.persistence.repository.music.AlbumRepository;
import com.walthersmulders.persistence.repository.music.ArtistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class ArtistAlbumService {
    private static final String ARTIST    = "Artist";
    private static final String ALBUM     = "Album";
    private static final String ARTIST_ID = "ArtistID";
    private static final String ALBUM_ID  = "AlbumID";

    private final ArtistRepository     artistRepository;
    private final ArtistMapper         artistMapper;
    private final AlbumRepository      albumRepository;
    private final AlbumMapper          albumMapper;
    private final GenreMusicRepository genreMusicRepository;

    public ArtistAlbumService(
            ArtistRepository artistRepository,
            ArtistMapper artistMapper,
            AlbumRepository albumRepository,
            AlbumMapper albumMapper,
            GenreMusicRepository genreMusicRepository
    ) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.genreMusicRepository = genreMusicRepository;
    }

    public Artist createArtist(ArtistUpsert artist) {
        log.info("Creating artist");

        boolean exists = artistRepository.exists(artist.firstName(), artist.lastName());

        if (exists) {
            log.error("Artist already exists");

            throw new EntityExistsException(
                    ARTIST, Map.ofEntries(
                    entry("first name", artist.firstName()),
                    entry("last name", artist.lastName())
            ));
        }

        ArtistEntity artistEntity = artistMapper.artistUpsertToEntity(artist);

        artistRepository.save(artistEntity);

        log.info("Artist created with id {}", artistEntity.getArtistID());

        return artistMapper.entityToArtist(artistEntity);
    }

    public List<Artist> getArtists() {
        log.info("Getting all artists without links");

        List<ArtistEntity> artists = artistRepository.findAll();

        log.info("Found {} artists", artists.size());

        return artists.isEmpty() ? Collections.emptyList()
                                 : artists.stream()
                                          .map(artistMapper::entityToArtist)
                                          .toList();
    }

    @Transactional(readOnly = true)
    public List<ArtistWithAlbums> getArtistsWithAlbums() {
        log.info("Getting all artists with albums");

        List<ArtistEntity> artistsWithLinks = artistRepository.fetchAllWithAlbums();

        log.info("Found {} artists", artistsWithLinks.size());

        return artistsWithLinks.isEmpty() ? Collections.emptyList()
                                          : artistsWithLinks.stream()
                                                            .map(artistMapper::entityToArtistWithAlbums)
                                                            .toList();
    }

    public Artist getArtist(UUID id) {
        log.info("Getting artist with artistID {}", id);

        ArtistEntity artist = artistRepository.
                findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        ARTIST,
                        Map.of(ARTIST_ID, id.toString())
                ));

        return artistMapper.entityToArtist(artist);
    }

    @Transactional(readOnly = true)
    public ArtistWithAlbums getArtistWithAlbums(UUID id) {
        log.info("Getting artist with albums with artistID {}", id);

        ArtistEntity artistWithAlbums = artistRepository
                .fetchArtistWithAlbums(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        ARTIST,
                        Map.of(ARTIST_ID, id.toString())
                ));

        return artistMapper.entityToArtistWithAlbums(artistWithAlbums);
    }

    public void updateArtist(UUID id, ArtistUpsert artist) {
        log.info("Updating artist with artistID {}", id);

        Optional<ArtistEntity> existingArtist = artistRepository.findById(id);

        if (existingArtist.isEmpty()) {
            log.error("Artist with id {} not found", id);

            throw new EntityNotFoundException(
                    ARTIST,
                    Map.of(ARTIST_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingArtist.get().checkUpdateDtoEqualsEntity(artist)
            && Objects.equals(existingArtist.get().getAdditionalName(), artist.additionalName())
        ) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            ArtistEntity updatedArtist = artistMapper.artistEntityUpdateMerge(
                    existingArtist.get(),
                    artist
            );

            artistRepository.save(updatedArtist);

            log.info("Artist updated with artistID {}", id);
        }
    }
}
