package com.walthersmulders.persistence.entity.music;

import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
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
@Table(name = "artist")
@Getter
@Setter
@NoArgsConstructor
public class ArtistEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "artist_id", nullable = false)
    private UUID artistID;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "additional_name", length = 100)
    private String additionalName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistAlbumEntity> albums;

    public boolean checkUpdateDtoEqualsEntity(ArtistUpsert artistUpsert) {
        return this.firstName.equals(artistUpsert.firstName()) &&
               this.lastName.equals(artistUpsert.lastName());
    }
}
