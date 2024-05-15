package com.walthersmulders.persistence.entity.music;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistAlbumID implements Serializable {
    @Column(name = "artist_id", nullable = false)
    private UUID artistID;

    @Column(name = "album_id", nullable = false)
    private UUID albumID;

    @Override
    public int hashCode() {
        return Objects.hash(artistID, albumID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtistAlbumID that = (ArtistAlbumID) o;

        return Objects.equals(artistID, that.artistID) &&
               Objects.equals(albumID, that.albumID);
    }
}
