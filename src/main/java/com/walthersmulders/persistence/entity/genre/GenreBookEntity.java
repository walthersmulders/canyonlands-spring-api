package com.walthersmulders.persistence.entity.genre;

import com.walthersmulders.mapstruct.dto.genre.book.GenreBookUpsert;
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

@Entity
@Table(name = "genre_book")
@Getter
@Setter
@NoArgsConstructor
public class GenreBookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "book_genre_id", nullable = false)
    private UUID bookGenreID;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "sub_genre", nullable = false, length = 500)
    private String subGenre;

    public boolean checkUpdateDtoEqualsEntity(GenreBookUpsert genreBookUpsert) {
        return this.genre.equals(genreBookUpsert.genre()) &&
               this.subGenre.equals(genreBookUpsert.subGenre());
    }
}
