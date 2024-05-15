package com.walthersmulders.persistence.entity.genre;

import com.walthersmulders.mapstruct.dto.genre.music.GenreMusicUpsert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Table containing the genres of music.
 */
@Entity
@Table(name = "genre_music")
@Getter
@Setter
@NoArgsConstructor
public class GenreMusicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "genre_music_id", nullable = false)
    private UUID genreMusicID;

    @Column(name = "genre", nullable = false, unique = true)
    private String genre;

    public boolean checkUpdateDtoEqualsEntity(GenreMusicUpsert genreMusicUpsert) {
        return this.genre.equals(genreMusicUpsert.genre());
    }
}
