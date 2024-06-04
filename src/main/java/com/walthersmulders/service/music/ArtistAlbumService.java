package com.walthersmulders.service.music;

import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ArtistAlbumService {
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
