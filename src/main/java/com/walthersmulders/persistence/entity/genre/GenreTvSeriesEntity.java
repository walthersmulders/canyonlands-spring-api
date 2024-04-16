package com.walthersmulders.persistence.entity.genre;

import com.walthersmulders.mapstruct.dto.genre.GenreTvSeriesUpsert;
import com.walthersmulders.persistence.entity.tvseries.TvSeriesGenreEntity;
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
@Table(name = "genre_tv_series")
@Getter
@Setter
@NoArgsConstructor
public class GenreTvSeriesEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "genre_tv_series_id", nullable = false)
    private UUID genreTvSeriesID;

    @Column(name = "genre", nullable = false, unique = true)
    private String genre;

    @Column(name = "external_id", nullable = false, unique = true)
    private Integer externalID;

    @OneToMany(mappedBy = "genreTvSeries", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TvSeriesGenreEntity> tvSeriesGenres;

    public boolean checkUpdateDtoEqualsEntity(GenreTvSeriesUpsert updateDto) {
        return this.genre.equals(updateDto.genre()) &&
               this.externalID.equals(updateDto.externalID());
    }
}
