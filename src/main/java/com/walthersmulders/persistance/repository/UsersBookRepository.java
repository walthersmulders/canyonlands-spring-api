package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.UsersBookEntity;
import com.walthersmulders.persistance.entity.UsersBookID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersBookRepository extends JpaRepository<UsersBookEntity, UsersBookID> {
    @Query("SELECT ub " +
           "FROM UsersBookEntity ub " +
           "JOIN FETCH ub.book b " +
           "WHERE ub.usersBookID.userID = :userID " +
           "AND b.bookID = :bookID")
    Optional<UsersBookEntity> fetchUsersBook(
            @Param(value = "userID") UUID userID,
            @Param(value = "bookID") UUID bookID
    );
}
