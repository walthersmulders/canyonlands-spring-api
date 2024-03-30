package com.walthersmulders.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_book")
@Getter
@Setter
@NoArgsConstructor
public class UsersBookEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsersBookID usersBookID;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookID")
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Column(name = "review", length = 5000, nullable = true)
    private String review;

    @Column(name = "rating", nullable = true)
    private Integer rating;

    public UsersBookEntity(UserEntity userEntity, BookEntity bookEntity) {
        this.user = userEntity;
        this.book = bookEntity;
        this.usersBookID = new UsersBookID(user.getUserID(), book.getBookID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, book);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersBookEntity that = (UsersBookEntity) o;

        return Objects.equals(user, that.user) && Objects.equals(book, that.book);
    }
}
