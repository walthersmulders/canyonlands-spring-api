package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(be) > 0 THEN true ELSE false END " +
           "FROM BookEntity be " +
           "WHERE be.title = :title")
    boolean existsByTitle(
            @Param(value = "title") String title
    );

    @Query("SELECT CASE WHEN COUNT(be) > 0 THEN true ELSE false END " +
           "FROM BookEntity be " +
           "WHERE be.isbn = :isbn")
    boolean existsByIsbn(
            @Param(value = "isbn") String isbn
    );

    @Query("SELECT CASE WHEN COUNT(be) > 0 THEN true ELSE false END " +
           "FROM BookEntity be " +
           "WHERE be.bookGenre.bookGenreID = :bookGenreID")
    boolean existsByBookGenreID(
            @Param(value = "bookGenreID") UUID bookGenreID
    );

    @Query("SELECT be " +
           "FROM BookEntity be " +
           "JOIN FETCH be.authors")
    List<BookEntity> fetchBooksWithLinks();

    @Query("SELECT be " +
           "FROM BookEntity be " +
           "JOIN FETCH be.authors " +
           "WHERE be.bookID = :bookID")
    Optional<BookEntity> fetchBookWithLinks(
            @Param(value = "bookID") UUID bookID
    );
}
