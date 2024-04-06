package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.mapstruct.dto.genremovie.GenreMovieUpsert;
import com.walthersmulders.persistance.entity.GenreMovieEntity;
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
