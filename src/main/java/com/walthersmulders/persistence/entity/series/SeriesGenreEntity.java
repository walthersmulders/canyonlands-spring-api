package com.walthersmulders.persistence.entity.series;

import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "series_genre")
@Getter
@Setter
@NoArgsConstructor
public class SeriesGenreEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SeriesGenreID seriesGenreID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreSeriesID")
    @JoinColumn(name = "genre_series_id")
    private GenreSeriesEntity genreSeries;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seriesID")
    @JoinColumn(name = "series_id")
    private SeriesEntity series;

    public SeriesGenreEntity(GenreSeriesEntity genreSeries, SeriesEntity series) {
        this.genreSeries = genreSeries;
        this.series = series;
        this.seriesGenreID = new SeriesGenreID(genreSeries.getGenreSeriesID(), series.getSeriesID());
    }
}
