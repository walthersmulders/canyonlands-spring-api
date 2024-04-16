package com.walthersmulders.persistence.entity.tvseries;

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
public class TvSeriesGenreID implements Serializable {
    @Column(name = "genre_tv_series_id", nullable = false)
    private UUID genreTvSeriesID;

    @Column(name = "tv_series_id", nullable = false)
    private UUID tvSeriesID;

    @Override
    public int hashCode() {
        return Objects.hash(genreTvSeriesID, tvSeriesID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TvSeriesGenreID that = (TvSeriesGenreID) o;

        return Objects.equals(genreTvSeriesID, that.genreTvSeriesID) &&
               Objects.equals(tvSeriesID, that.tvSeriesID);
    }
}
