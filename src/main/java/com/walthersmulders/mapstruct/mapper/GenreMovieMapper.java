package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genre.movie.GenreMovieUpsert;
import com.walthersmulders.persistence.entity.genre.GenreMovieEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMovieMapper {
    GenreMovie entityToGenreMovie(GenreMovieEntity entity);

    GenreMovieEntity genreMovieUpsertToEntity(GenreMovieUpsert genreMovieUpsert);

    @InheritConfiguration
    GenreMovieEntity genreMovieEntityUpdateMerge(
            @MappingTarget GenreMovieEntity entity,
            GenreMovieUpsert genreMovieUpsert
    );
}
