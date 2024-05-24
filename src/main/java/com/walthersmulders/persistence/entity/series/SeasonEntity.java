package com.walthersmulders.persistence.entity.series;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "season")
@Getter
@Setter
@NoArgsConstructor
public class SeasonEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "season_id", nullable = false)
    private UUID seasonID;

    @Column(name = "title", nullable = false, length = 500)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private SeriesEntity series;

    @Override
    public int hashCode() {
        return Objects.hash(seasonID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return seasonID != null && seasonID.equals(((SeasonEntity) o).seasonID);
    }
}
