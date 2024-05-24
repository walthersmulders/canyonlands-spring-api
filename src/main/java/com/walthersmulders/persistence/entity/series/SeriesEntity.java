package com.walthersmulders.persistence.entity.series;

import com.walthersmulders.mapstruct.dto.series.SeriesUpsert;
import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
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
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
public class SeriesEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "series_id", nullable = false)
    private UUID seriesID;

    @Column(name = "title", nullable = false, unique = true, length = 500)
    private String title;

    @Column(name = "plot", nullable = false)
    private String plot;

    @Column(name = "poster", nullable = false, length = 1000)
    private String poster;

    @Column(name = "date_released", nullable = false)
    private LocalDate dateReleased;

    @Column(name = "date_added", nullable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeriesGenreEntity> seriesGenres = new ArrayList<>();

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonEntity> seasons = new ArrayList<>();

    public void addSeriesGenre(GenreSeriesEntity genreSeries) {
        SeriesGenreEntity seriesGenre = new SeriesGenreEntity(genreSeries, this);
        seriesGenres.add(seriesGenre);
    }

    public void removeSeriesGenre(GenreSeriesEntity genreSeries) {
        for (Iterator<SeriesGenreEntity> iterator = seriesGenres.iterator(); iterator.hasNext(); ) {
            SeriesGenreEntity entity = iterator.next();

            if (entity.getSeries().equals(this) && entity.getGenreSeries().equals(genreSeries)) {
                iterator.remove();

                entity.setSeries(null);
                entity.setGenreSeries(null);
            }

        }
    }

    public void addSeason(SeasonEntity season) {
        this.seasons.add(season);
        season.setSeries(this);
    }

    public void addSeasons(List<SeasonEntity> seasons) {
        this.seasons.addAll(seasons);
        seasons.forEach(item -> item.setSeries(this));
    }

    public void removeSeason(SeasonEntity season) {
        season.setSeries(null);
        this.seasons.remove(season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seriesID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return seriesID != null && seriesID.equals(((SeriesEntity) o).seriesID);
    }

    public boolean checkUpdateDtoEqualsEntity(SeriesUpsert seriesUpsert) {
        return this.title.equals(seriesUpsert.title()) &&
               this.plot.equals(seriesUpsert.plot()) &&
               this.poster.equals(seriesUpsert.poster()) &&
               this.dateReleased.equals(seriesUpsert.dateReleased());
    }
}
