package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.users.movie.UsersMovie;
import com.walthersmulders.persistence.entity.users.movie.UsersMovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMovieMapper {
    UsersMovie entityToUsersMovie(UsersMovieEntity entity);
}
