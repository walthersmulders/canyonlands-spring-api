package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM AuthorEntity a " +
           "WHERE a.firstName = :firstName " +
           "AND a.lastName = :lastName")
    boolean exists(
            @Param(value = "firstName") String firstName,
            @Param(value = "lastName") String lastName
    );

    @Query("SELECT ae " +
           "FROM AuthorEntity ae " +
           "JOIN FETCH ae.books")
    List<AuthorEntity> fetchAllWithBooks();

    @Query("SELECT ae " +
           "FROM AuthorEntity ae " +
           "JOIN FETCH ae.books " +
           "WHERE ae.authorID = :authorID")
    Optional<AuthorEntity> fetchAuthorWithBooks(
            @Param(value = "authorID") UUID authorID
    );
}
