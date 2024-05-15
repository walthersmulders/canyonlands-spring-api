package com.walthersmulders.persistence.entity.music;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "artist_album")
@Getter
@Setter
@NoArgsConstructor
public class ArtistAlbumEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ArtistAlbumID artistAlbumID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistID")
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("albumID")
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    public ArtistAlbumEntity(ArtistEntity artistEntity, AlbumEntity albumEntity) {
        this.artist = artistEntity;
        this.album = albumEntity;
        this.artistAlbumID = new ArtistAlbumID(artist.getArtistID(), album.getAlbumID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, album);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtistAlbumEntity that = (ArtistAlbumEntity) o;

        return Objects.equals(artist, that.artist) && Objects.equals(album, that.album);
    }
}
