package com.walthersmulders.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
public class BookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(name = "book_id", nullable = false)
    private UUID bookID;

    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Column(name = "isbn", unique = true, nullable = false, length = 20)
    private String isbn;

    @Column(name = "pages", nullable = false)
    private Integer pages;

    @Column(name = "plot", nullable = false)
    private String plot;

    @Column(name = "cover", nullable = false, length = 1000)
    private String cover = "default_book_cover.jpg";

    @Column(name = "date_published", nullable = false)
    private LocalDate datePublished;

    @Column(name = "date_added", nullable = false)
    private LocalDateTime dateAdded;

    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorBookEntity> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersBookEntity> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_genre_id")
    private BookGenreEntity bookGenre;

    /**
     * TODO :: IMPL this
     * public void addAuthorEntity(AuthorEntity authorEntity) {
     *         AuthorBookEntity authorBookEntity = new AuthorBookEntity(authorEntity, this);
     *         authors.add(authorBookEntity);
     *     }
     * <p>
     * public void removeAuthorEntity(AuthorEntity authorEntity) {
     * for (Iterator<AuthorBookEntity> iterator = authors.iterator(); iterator.hasNext(); ) {
     * AuthorBookEntity authorBookEntity = iterator.next();
     * <p>
     * if (authorBookEntity.getBook().equals(this) && authorBookEntity.getAuthor().equals(authorEntity)) {
     * iterator.remove();
     * <p>
     * authorBookEntity.setBook(null);
     * authorBookEntity.setAuthor(null);
     * }
     * }
     * }
     */

    @Override
    public int hashCode() {
        return Objects.hashCode(this.bookID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return bookID != null && ((BookEntity) o).bookID.equals(bookID);
    }
}
