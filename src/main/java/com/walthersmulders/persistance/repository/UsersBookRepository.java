package com.walthersmulders.persistance.repository;

import com.walthersmulders.persistance.entity.UsersBookEntity;
import com.walthersmulders.persistance.entity.UsersBookID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersBookRepository extends JpaRepository<UsersBookEntity, UsersBookID> {
}
