package com.walthersmulders.persistence.entity.music;

import com.walthersmulders.mapstruct.dto.album.AlbumUpsert;
import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import com.walthersmulders.persistence.entity.users.music.UsersMusicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
public class AlbumEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "album_id", nullable = false)
    private UUID albumID;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "cover", nullable = false, length = 1000)
    private String cover;

    @Column(name = "date_published", nullable = false)
    private LocalDate datePublished;

    @Column(name = "date_added", nullable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistAlbumEntity> artists = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersMusicEntity> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_music_id")
    private GenreMusicEntity genreMusic;

    public void addArtist(ArtistEntity artist) {
        ArtistAlbumEntity artistAlbum = new ArtistAlbumEntity(artist, this);
        artists.add(artistAlbum);
    }

    // TODO :: Remove artists helper methods

    public boolean checkUpdateDtoEqualsEntity(AlbumUpsert albumUpsert) {
        return this.title.equals(albumUpsert.title()) &&
               this.cover.equals(albumUpsert.cover()) &&
               this.datePublished.equals(albumUpsert.datePublished());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.albumID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return albumID != null && ((AlbumEntity) o).albumID.equals(albumID);
    }
}
