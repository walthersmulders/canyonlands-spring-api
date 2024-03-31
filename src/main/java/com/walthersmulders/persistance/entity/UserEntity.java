package com.walthersmulders.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userID;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email_address", unique = true, nullable = false, length = 500)
    private String emailAddress;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersBookEntity> books;

    public void addBookToUserLibrary(BookEntity book, Integer rating, String review) {
        UsersBookEntity usersBook = new UsersBookEntity(this, book, rating, review);
        books.add(usersBook);
    }

    public void addBookToUserLibrary(BookEntity book, Integer rating) {
        UsersBookEntity usersBook = new UsersBookEntity(this, book, rating);
        books.add(usersBook);
    }


    // TODO :: Implement this
    //    public void removeBookFromUserLibrary(BookEntity book) {
    //        for (Iterator<UsersBookEntity> iterator = books.iterator(); iterator.hasNext(); ) {
    //            UsersBookEntity usersBook = iterator.next();
    //
    //            if (usersBook.getUser().equals(this) && usersBook.getBook().equals(book)) {
    //                iterator.remove();
    //                usersBook.setBook(null);
    //                usersBook.setUser(null);
    //            }
    //        }
    //    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.userID);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        return userID != null && ((UserEntity) o).userID.equals(userID);
    }
}
