package com.walthersmulders.persistence.entity.users.movie;

import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_movie")
@Getter
@Setter
@NoArgsConstructor
public class UsersMovieEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsersMovieID usersMovieID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieID")
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @Column(name = "review", length = 5000)
    private String review;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    public UsersMovieEntity(UserEntity user, MovieEntity movie, Integer rating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.usersMovieID = new UsersMovieID(user.getUserID(), movie.getMovieID());
    }

    public UsersMovieEntity(UserEntity user, MovieEntity movie, Integer rating, String review) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.review = review;
        this.usersMovieID = new UsersMovieID(user.getUserID(), movie.getMovieID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, movie);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersMovieEntity that = (UsersMovieEntity) o;

        return Objects.equals(user, that.user) &&
               Objects.equals(movie, that.movie);
    }
}
