package com.walthersmulders.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookID implements Serializable {
    @Column(name = "author_id", nullable = false)
    private UUID authorID;

    @Column(name = "book_id", nullable = false)
    private UUID bookID;

    @Override
    public int hashCode() {
        return Objects.hash(authorID, bookID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorBookID that = (AuthorBookID) o;

        return Objects.equals(authorID, that.authorID) &&
               Objects.equals(bookID, that.bookID);
    }
}
