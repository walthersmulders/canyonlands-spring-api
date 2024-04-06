package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.mapstruct.dto.movie.Movie;
import com.walthersmulders.mapstruct.dto.movie.MovieUpsert;
import com.walthersmulders.mapstruct.dto.movie.MovieWithLinks;
import com.walthersmulders.persistance.entity.MovieEntity;
import com.walthersmulders.persistance.entity.MovieGenreEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovieMapper {
    MovieEntity movieUpsertToEntity(MovieUpsert movie);

    Movie entityToMovie(MovieEntity entity);

    @Mapping(target = "genres", source = "entity", qualifiedByName = "genreMovieListToGenreMovieList")
    MovieWithLinks entityToMovieWithLinks(MovieEntity entity);

    @Named(value = "genreMovieListToGenreMovieList")
    default List<GenreMovie> genreMovieListToGenreMovieList(MovieEntity entity) {
        GenreMovieMapperImpl genreMovieMapper = new GenreMovieMapperImpl();

        List<GenreMovie> list = new ArrayList<>();

        entity.getMovieGenres()
              .stream()
              .map(MovieGenreEntity::getGenreMovie)
              .forEach(genre -> list.add(genreMovieMapper.entityToGenreMovie(genre)));

        return list;
    }

    @InheritConfiguration
    MovieEntity movieEntityUpdateMerge(
            @MappingTarget MovieEntity movieEntity,
            MovieUpsert movieUpsert
    );
}
