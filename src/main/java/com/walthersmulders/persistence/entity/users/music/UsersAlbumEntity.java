package com.walthersmulders.persistence.entity.users.music;

import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_album")
@Getter
@Setter
@NoArgsConstructor
public class UsersAlbumEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsersMusicID usersMusicID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("albumID")
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    @Column(name = "review", length = 5000)
    private String review;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    public UsersAlbumEntity(UserEntity user, AlbumEntity album, Integer rating) {
        this.user = user;
        this.album = album;
        this.rating = rating;
        this.usersMusicID = new UsersMusicID(user.getUserID(), album.getAlbumID());
    }

    public UsersAlbumEntity(UserEntity user, AlbumEntity album, Integer rating, String review) {
        this.user = user;
        this.album = album;
        this.rating = rating;
        this.review = review;
        this.usersMusicID = new UsersMusicID(user.getUserID(), album.getAlbumID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, album);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersAlbumEntity that = (UsersAlbumEntity) o;

        return Objects.equals(user, that.user) && Objects.equals(album, that.album);
    }
}
