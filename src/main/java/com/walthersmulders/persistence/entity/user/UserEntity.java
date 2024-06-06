package com.walthersmulders.persistence.entity.user;

import com.walthersmulders.persistence.entity.book.BookEntity;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.users.book.UsersBookEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import com.walthersmulders.persistence.entity.users.music.UsersMusicEntity;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userID;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email_address", unique = true, nullable = false, length = 500)
    private String emailAddress;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersBookEntity> books;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersMovieEntity> movies;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersSeriesEntity> series;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersMusicEntity> albums;

    public void addBookToUserLibrary(BookEntity book, Integer rating, String review) {
        UsersBookEntity usersBook = new UsersBookEntity(this, book, rating, review);
        books.add(usersBook);
    }

    public void addBookToUserLibrary(BookEntity book, Integer rating) {
        UsersBookEntity usersBook = new UsersBookEntity(this, book, rating);
        books.add(usersBook);
    }

    public void addMovieToUserLibrary(MovieEntity movie, Integer rating, String review) {
        UsersMovieEntity usersMovie = new UsersMovieEntity(this, movie, rating, review);
        movies.add(usersMovie);
    }

    public void addMovieToUserLibrary(MovieEntity movie, Integer rating) {
        UsersMovieEntity usersMovie = new UsersMovieEntity(this, movie, rating);
        movies.add(usersMovie);
    }

    public void addSeriesToUserLibrary(SeriesEntity seriesEntity, Integer rating, String review) {
        UsersSeriesEntity usersSeries = new UsersSeriesEntity(this, seriesEntity, review, rating);
        series.add(usersSeries);
    }

    public void addSeriesToUserLibrary(SeriesEntity seriesEntity, Integer rating) {
        UsersSeriesEntity usersSeries = new UsersSeriesEntity(this, seriesEntity, rating);
        series.add(usersSeries);
    }

    public void addAlbumToUserLibrary(AlbumEntity albumEntity, Integer rating) {
        UsersMusicEntity usersAlbums = new UsersMusicEntity(this, albumEntity, rating);
        albums.add(usersAlbums);
    }

    public void addAlbumToUserLibrary(AlbumEntity albumEntity, Integer rating, String review) {
        UsersMusicEntity usersAlbums = new UsersMusicEntity(this, albumEntity, rating, review);
        albums.add(usersAlbums);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.userID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return userID != null && ((UserEntity) o).userID.equals(userID);
    }
}
