package com.walthersmulders.persistence.entity.book;

import com.walthersmulders.mapstruct.dto.book.BookUpsert;
import com.walthersmulders.persistence.entity.genre.GenreBookEntity;
import com.walthersmulders.persistence.entity.users.book.UsersBookEntity;
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
    private String cover;

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
    private GenreBookEntity genreBook;

    public void addAuthor(AuthorEntity author) {
        AuthorBookEntity authorBook = new AuthorBookEntity(author, this);
        authors.add(authorBook);
    }

    public void removeAuthor(AuthorEntity author) {
        for (Iterator<AuthorBookEntity> iterator = authors.iterator(); iterator.hasNext(); ) {
            AuthorBookEntity authorBookEntity = iterator.next();

            if (authorBookEntity.getBook().equals(this) && authorBookEntity.getAuthor().equals(author)) {
                iterator.remove();

                authorBookEntity.setBook(null);
                authorBookEntity.setAuthor(null);
            }
        }
    }

    public boolean checkUpdateDtoEqualsEntity(BookUpsert bookUpsert) {
        return this.title.equals(bookUpsert.title()) &&
               this.isbn.equals(bookUpsert.isbn()) &&
               this.pages.equals(bookUpsert.pages()) &&
               this.plot.equals(bookUpsert.plot()) &&
               this.cover.equals(bookUpsert.cover()) &&
               this.datePublished.equals(bookUpsert.datePublished());
    }

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
