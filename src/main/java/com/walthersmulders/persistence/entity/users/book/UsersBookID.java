package com.walthersmulders.persistence.entity.users.book;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersBookID implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "book_id")
    private UUID bookID;

    @Override
    public int hashCode() {
        return Objects.hash(userID, bookID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsersBookID that = (UsersBookID) o;

        return Objects.equals(userID, that.userID) &&
               Objects.equals(bookID, that.bookID);
    }
}
