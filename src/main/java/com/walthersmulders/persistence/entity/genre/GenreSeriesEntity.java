package com.walthersmulders.persistence.entity.genre;

import com.walthersmulders.mapstruct.dto.genre.series.GenreSeriesUpsert;
import com.walthersmulders.persistence.entity.series.SeriesGenreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "genre_series")
@Getter
@Setter
@NoArgsConstructor
public class GenreSeriesEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "genre_series_id", nullable = false)
    private UUID genreSeriesID;

    @Column(name = "genre", nullable = false, unique = true)
    private String genre;

    @Column(name = "external_id", nullable = false, unique = true)
    private Integer externalID;

    @OneToMany(mappedBy = "genreSeries", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeriesGenreEntity> seriesGenres;

    public boolean checkUpdateDtoEqualsEntity(GenreSeriesUpsert updateDto) {
        return this.genre.equals(updateDto.genre()) &&
               this.externalID.equals(updateDto.externalID());
    }
}
