package com.walthersmulders.persistence.entity.tvseries;

import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "tv_series_genre")
@Getter
@Setter
@NoArgsConstructor
public class TvSeriesGenreEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TvSeriesGenreID tvSeriesGenreID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreTvSeriesID")
    @JoinColumn(name = "genre_tv_series_id")
    private GenreTvSeriesEntity genreTvSeries;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tvSeriesID")
    @JoinColumn(name = "tv_series_id")
    private TvSeriesEntity tvSeries;

    public TvSeriesGenreEntity(GenreTvSeriesEntity genreTvSeries, TvSeriesEntity tvSeries) {
        this.genreTvSeries = genreTvSeries;
        this.tvSeries = tvSeries;
        this.tvSeriesGenreID = new TvSeriesGenreID(genreTvSeries.getGenreTvSeriesID(), tvSeries.getTvSeriesID());
    }
}
