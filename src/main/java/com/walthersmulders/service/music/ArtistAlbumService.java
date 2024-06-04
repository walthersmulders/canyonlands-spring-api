package com.walthersmulders.service.music;

import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import com.walthersmulders.mapstruct.mapper.AlbumMapper;
import com.walthersmulders.mapstruct.mapper.ArtistMapper;
import com.walthersmulders.persistence.repository.genre.GenreMusicRepository;
import com.walthersmulders.persistence.repository.music.AlbumRepository;
import com.walthersmulders.persistence.repository.music.ArtistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        return null;
    }

    public List<Artist> getArtists() {
        return null;
    }

    public List<ArtistWithAlbums> getArtistsWithAlbums() {
        return null;
    }

    public Artist getArtist(UUID id) {
        return null;
    }

    public ArtistWithAlbums getArtistWithAlbums(UUID id) {
        return null;
    }

    public void updateArtist(UUID id, ArtistUpsert artist) {

    }
}
