package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.persistance.entity.GenreMovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMovieMapper {
    GenreMovie entityToGenreMovie(GenreMovieEntity entity);
}
