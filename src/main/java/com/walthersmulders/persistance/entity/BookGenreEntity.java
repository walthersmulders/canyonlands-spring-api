package com.walthersmulders.persistance.entity;

import com.walthersmulders.mapstruct.dto.bookgenre.BookGenreNoID;
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
@Table(name = "book_genre")
@Getter
@Setter
@NoArgsConstructor
public class BookGenreEntity implements Serializable {
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

    public boolean checkUpdateDtoEqualsEntity(BookGenreNoID bookGenreNoID) {
        return this.genre.equals(bookGenreNoID.genre()) &&
               this.subGenre.equals(bookGenreNoID.subGenre());
    }
}
