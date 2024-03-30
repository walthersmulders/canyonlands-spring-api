package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.AuthorBookEntity;
import com.walthersmulders.persistance.entity.AuthorBookID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorBookRepository extends JpaRepository<AuthorBookEntity, AuthorBookID> {
}
