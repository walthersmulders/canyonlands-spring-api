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
public class MusicGenreID implements Serializable {
    @Column(name = "genre_music_id", nullable = false)
    private UUID genreMusicID;

    @Column(name = "album_id", nullable = false)
    private UUID albumID;

    @Override
    public int hashCode() {
        return Objects.hash(genreMusicID, albumID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MusicGenreID that = (MusicGenreID) o;

        return Objects.equals(genreMusicID, that.genreMusicID) &&
               Objects.equals(albumID, that.albumID);
    }
}
