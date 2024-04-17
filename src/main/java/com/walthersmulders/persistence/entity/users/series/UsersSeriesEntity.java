package com.walthersmulders.persistence.entity.users.series;

import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_series")
@Getter
@Setter
@NoArgsConstructor
public class UsersSeriesEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsersSeriesID usersSeriesID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seriesID")
    @JoinColumn(name = "series_id")
    private SeriesEntity series;

    @Column(name = "review", length = 5000)
    private String review;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    public UsersSeriesEntity(UserEntity user, SeriesEntity series, Integer rating) {
        this.user = user;
        this.series = series;
        this.rating = rating;
        this.usersSeriesID = new UsersSeriesID(user.getUserID(), series.getSeriesID());
    }

    public UsersSeriesEntity(UserEntity user, SeriesEntity series, String review, Integer rating) {
        this.user = user;
        this.series = series;
        this.review = review;
        this.rating = rating;
        this.usersSeriesID = new UsersSeriesID(user.getUserID(), series.getSeriesID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, series);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersSeriesEntity that = (UsersSeriesEntity) o;

        return Objects.equals(usersSeriesID, that.usersSeriesID) &&
               Objects.equals(series, that.series);
    }
}
