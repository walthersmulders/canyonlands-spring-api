package com.walthersmulders.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "author_book")
@Getter
@Setter
@NoArgsConstructor
public class AuthorBookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AuthorBookID authorBookID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("authorID")
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookID")
    @JoinColumn(name = "book_id")
    private BookEntity book;

    public AuthorBookEntity(AuthorEntity authorEntity, BookEntity bookEntity) {
        this.author = authorEntity;
        this.book = bookEntity;
        this.authorBookID = new AuthorBookID(author.getAuthorID(), book.getBookID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, book);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorBookEntity that = (AuthorBookEntity) o;

        return Objects.equals(author, that.author) && Objects.equals(book, that.book);
    }
}
