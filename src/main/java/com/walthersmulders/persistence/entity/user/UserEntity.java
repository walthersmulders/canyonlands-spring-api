package com.walthersmulders.persistence.entity.user;

import com.walthersmulders.persistence.entity.book.BookEntity;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.users.book.UsersBookEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
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
