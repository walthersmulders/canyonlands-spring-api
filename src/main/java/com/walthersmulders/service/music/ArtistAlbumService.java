package com.walthersmulders.service.music;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.album.Album;
import com.walthersmulders.mapstruct.dto.album.AlbumUpsert;
import com.walthersmulders.mapstruct.dto.album.AlbumWithLinks;
import com.walthersmulders.mapstruct.dto.album.AlbumWithLinksUpsert;
import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import com.walthersmulders.mapstruct.mapper.AlbumMapper;
import com.walthersmulders.mapstruct.mapper.ArtistMapper;
import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.music.ArtistAlbumEntity;
import com.walthersmulders.persistence.entity.music.ArtistEntity;
import com.walthersmulders.persistence.repository.genre.GenreMusicRepository;
import com.walthersmulders.persistence.repository.music.AlbumRepository;
import com.walthersmulders.persistence.repository.music.ArtistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    public AlbumWithLinks createAlbumWithArtists(AlbumWithLinksUpsert albumWithLinksUpsert) {
        log.info("Creating album with artists");
        log.info("Check if album with title {} already exists", albumWithLinksUpsert.album().title());

        boolean existsByTitle = albumRepository.existsByTitle(albumWithLinksUpsert.album().title());

        if (existsByTitle) {
            log.error("Album with title {} already exists", albumWithLinksUpsert.album().title());

            throw new EntityExistsException(
                    ALBUM,
                    Map.of("title", albumWithLinksUpsert.album().title())
            );
        }

        Optional<GenreMusicEntity> musicGenre = genreMusicRepository.findById(
                albumWithLinksUpsert.genreMusicID()
        );

        if (musicGenre.isEmpty()) {
            log.error("Music genre with id {} not found", albumWithLinksUpsert.genreMusicID());

            throw new EntityNotFoundException(
                    "Music Genre",
                    Map.of("genreMusicID", String.valueOf(albumWithLinksUpsert.genreMusicID()))
            );
        }

        AlbumEntity album = albumMapper.albumUpsertToEntity(albumWithLinksUpsert.album());

        album.setGenreMusic(musicGenre.get());
        album.setDateAdded(LocalDateTime.now());
        album.setDateUpdated(LocalDateTime.now());

        albumWithLinksUpsert.artistIDs()
                            .stream()
                            .map(artistRepository::findById)
                            .forEach(artist -> artist.ifPresent(album::addArtist));

        // TODO :: check if artists not found for Ids and assoc with album empty then thr error

        albumRepository.save(album);

        return albumMapper.entityToAlbumWithLinks(album);
    }

    public List<Album> getAlbums() {
        log.info("Getting all albums");

        List<AlbumEntity> albums = albumRepository.findAll();

        log.info("Found {} albums", albums.size());

        return albums.isEmpty() ? Collections.emptyList()
                                : albums.stream()
                                        .map(albumMapper::entityToAlbum)
                                        .toList();
    }

    @Transactional(readOnly = true)
    public List<AlbumWithLinks> getAlbumsWithLinks() {
        log.info("Getting all albums with links");

        List<AlbumEntity> albumsWithLinks = albumRepository.fetchAlbumsWithLinks();

        log.info("Found {} albums with links", albumsWithLinks.size());

        return albumsWithLinks.isEmpty() ? Collections.emptyList()
                                         : albumsWithLinks.stream()
                                                          .map(albumMapper::entityToAlbumWithLinks)
                                                          .toList();
    }

    public Album getAlbum(UUID id) {
        log.info("Getting album with albumID {}", id);

        AlbumEntity album = albumRepository.findById(id)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   ALBUM,
                                                   Map.of(ALBUM_ID, id.toString())
                                           ));

        return albumMapper.entityToAlbum(album);
    }

    @Transactional(readOnly = true)
    public AlbumWithLinks getAlbumWithLinks(UUID id) {
        log.info("Getting album with links for albumID {}", id);

        AlbumEntity albumWithLinks = albumRepository.fetchAlbumWithLinks(id)
                                                    .orElseThrow(() -> new EntityNotFoundException(
                                                            ALBUM,
                                                            Map.of(ALBUM_ID, id.toString())
                                                    ));

        return albumMapper.entityToAlbumWithLinks(albumWithLinks);
    }

    public void updateAlbum(UUID id, AlbumUpsert albumUpsert) {
        log.info("Updating album with id {}", id);

        Optional<AlbumEntity> existingAlbum = albumRepository.findById(id);

        if (existingAlbum.isEmpty()) {
            log.error("Album with id {} not found", id);

            throw new EntityNotFoundException(
                    ALBUM,
                    Map.of(ALBUM_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingAlbum.get().checkUpdateDtoEqualsEntity(albumUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            boolean existsByTitle = false;

            log.info("Check if album with title {} already exists", albumUpsert.title());

            if (!existingAlbum.get().getTitle().equals(albumUpsert.title())) {
                existsByTitle = albumRepository.existsByTitle(albumUpsert.title());
            }

            if (existsByTitle) {
                log.error("Album with title {} already exists", albumUpsert.title());

                throw new EntityExistsException(
                        ALBUM,
                        Map.of("title", albumUpsert.title())
                );
            }

            AlbumEntity updatedAlbum = albumMapper.albumEntityUpdateMerge(
                    existingAlbum.get(),
                    albumUpsert
            );

            updatedAlbum.setDateUpdated(LocalDateTime.now());

            albumRepository.save(updatedAlbum);

            log.info("Album updated with id {}", id);
        }
    }

    @Transactional
    public void addArtistToAlbum(UUID albumID, UUID artistID) {
        log.info("Adding artist to album with id {}", albumID);

        Optional<AlbumEntity> album = albumRepository.fetchAlbumWithArtists(albumID);

        if (album.isEmpty()) {
            log.error("Album with id {} not found", albumID);

            throw new EntityNotFoundException(
                    ALBUM,
                    Map.of(ALBUM_ID, albumID.toString())
            );
        }

        ArtistAlbumEntity artistAlbum = album.get().getArtists()
                                             .stream()
                                             .filter(artist -> artist.getArtist().getArtistID().equals(artistID))
                                             .findFirst()
                                             .orElse(null);

        if (artistAlbum != null) {
            log.error("Artist with artistID {} already exists in albums list", artistID);

            throw new EntityExistsException(
                    ARTIST,
                    Map.of(ARTIST_ID, artistID.toString())
            );
        }

        Optional<ArtistEntity> artist = artistRepository.findById(artistID);

        if (artist.isEmpty()) {
            log.error("Artist with id {} not found", artistID);

            throw new EntityNotFoundException(
                    ARTIST,
                    Map.of(ARTIST_ID, artistID.toString())
            );
        }

        album.get().addArtist(artist.get());

        log.info("Artist with artistID {} added to album with albumID {}", artistID, albumID);
    }

    @Transactional
    public void removeArtistFromAlbum(UUID albumID, UUID artistID) {
        log.info("Removing artist with artistID {} from album with id {}", artistID, albumID);

        Optional<AlbumEntity> album = albumRepository.fetchAlbumWithArtists(albumID);

        if (album.isEmpty()) {
            log.error("Album with id {} not found", albumID);

            throw new EntityNotFoundException(
                    ALBUM,
                    Map.of(ALBUM_ID, albumID.toString())
            );
        }

        ArtistAlbumEntity artistAlbum = album.get().getArtists()
                                             .stream()
                                             .filter(artist -> artist.getArtist().getArtistID().equals(artistID))
                                             .findFirst()
                                             .orElse(null);

        if (artistAlbum != null) {
            if (artistAlbum.getAlbum().getArtists().size() <= 1) {
                log.error("An album must have at least one artist");

                throw new GenericBadRequestException("An album must have at least one artist");
            }

            artistAlbum.getAlbum().removeArtist(artistAlbum.getArtist());

            log.info("Artist with artistID {} removed from album with albumID {}", artistID, albumID);
        }
    }

    public void updateAlbumGenre(UUID albumID, UUID genreID) {
        log.info("Updating album genre with genreID {} for album with albumID {}", genreID, albumID);

        Optional<AlbumEntity> album = albumRepository.findById(albumID);

        if (album.isEmpty()) {
            log.error("Album with id {} not found", albumID);

            throw new EntityNotFoundException(
                    ALBUM,
                    Map.of(ALBUM_ID, albumID.toString())
            );
        }

        Optional<GenreMusicEntity> genreMusic = genreMusicRepository.findById(genreID);

        if (genreMusic.isEmpty()) {
            log.error("Genre with id {} not found", genreID);

            throw new EntityNotFoundException(
                    "Genre Music",
                    Map.of("genreMusicID", genreID.toString())
            );
        }

        album.get().setGenreMusic(genreMusic.get());

        albumRepository.save(album.get());

        log.info("Album genre with genreID {} updated for album with albumID {}", genreID, albumID);
    }
}
