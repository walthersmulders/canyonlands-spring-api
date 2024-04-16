package com.walthersmulders.persistence.entity.tvseries;

import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesUpsert;
import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tv_series")
@Getter
@Setter
@NoArgsConstructor
public class TvSeriesEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "tv_series_id", nullable = false)
    private UUID tvSeriesID;

    @Column(name = "title", nullable = false, unique = true, length = 500)
    private String title;

    @Column(name = "plot", nullable = false)
    private String plot;

    @Column(name = "external_id", unique = true, nullable = false)
    private Integer externalID;

    @Column(name = "poster", nullable = false, length = 1000)
    private String poster;

    @Column(name = "date_released", nullable = false)
    private LocalDate dateReleased;

    @Column(name = "date_added", nullable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "tvSeries", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TvSeriesGenreEntity> tvSeriesGenres = new ArrayList<>();

    public void addTvSeriesGenre(GenreTvSeriesEntity genreTvSeries) {
        TvSeriesGenreEntity tvSeriesGenre = new TvSeriesGenreEntity(genreTvSeries, this);
        tvSeriesGenres.add(tvSeriesGenre);
    }

    public void removeTvSeriesGenre(GenreTvSeriesEntity genreTvSeries) {
        for (Iterator<TvSeriesGenreEntity> iterator = tvSeriesGenres.iterator(); iterator.hasNext(); ) {
            TvSeriesGenreEntity entity = iterator.next();

            if (entity.getTvSeries().equals(this) && entity.getGenreTvSeries().equals(genreTvSeries)) {
                iterator.remove();

                entity.setTvSeries(null);
                entity.setGenreTvSeries(null);
            }

        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tvSeriesID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return tvSeriesID != null && tvSeriesID.equals(((TvSeriesEntity) o).tvSeriesID);
    }

    public boolean checkUpdateDtoEqualsEntity(TvSeriesUpsert tvSeriesUpsert) {
        return this.title.equals(tvSeriesUpsert.title()) &&
               this.plot.equals(tvSeriesUpsert.plot()) &&
               this.poster.equals(tvSeriesUpsert.poster()) &&
               this.externalID.equals(tvSeriesUpsert.externalID()) &&
               this.dateReleased.equals(tvSeriesUpsert.dateReleased());
    }
}
