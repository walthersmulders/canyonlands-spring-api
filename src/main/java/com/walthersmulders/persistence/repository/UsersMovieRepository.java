package com.walthersmulders.persistence.repository;

import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMovieRepository extends JpaRepository<UsersMovieEntity, UsersMovieID> {
}
