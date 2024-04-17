package com.walthersmulders.persistence.entity.series;

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
public class SeriesGenreID implements Serializable {
    @Column(name = "genre_series_id", nullable = false)
    private UUID genreSeriesID;

    @Column(name = "series_id", nullable = false)
    private UUID seriesID;

    @Override
    public int hashCode() {
        return Objects.hash(genreSeriesID, seriesID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SeriesGenreID that = (SeriesGenreID) o;

        return Objects.equals(genreSeriesID, that.genreSeriesID) &&
               Objects.equals(seriesID, that.seriesID);
    }
}
