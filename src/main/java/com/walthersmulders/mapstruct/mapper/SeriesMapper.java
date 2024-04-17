package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;
import com.walthersmulders.mapstruct.dto.series.Series;
import com.walthersmulders.mapstruct.dto.series.SeriesUpsert;
import com.walthersmulders.mapstruct.dto.series.SeriesWithLinks;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.series.SeriesGenreEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeriesMapper {
    SeriesEntity seriesUpsertToEntity(SeriesUpsert seriesUpsert);

    Series entityToSeries(SeriesEntity entity);

    @Mapping(target = "genres", source = "entity", qualifiedByName = "genreSeriesListToGenreSeriesList")
    SeriesWithLinks entityToSeriesWithLinks(SeriesEntity entity);

    @Named(value = "genreSeriesListToGenreSeriesList")
    default List<GenreSeries> genreSeriesListToGenreSeriesList(SeriesEntity entity) {
        GenreSeriesMapperImpl genreSeriesMapper = new GenreSeriesMapperImpl();

        List<GenreSeries> list = new ArrayList<>();

        entity.getSeriesGenres()
              .stream()
              .map(SeriesGenreEntity::getGenreSeries)
              .forEach(genre -> list.add(genreSeriesMapper.entityToGenreSeries(genre)));

        return list;
    }

    @InheritConfiguration
    SeriesEntity seriesEntityUpdateMerge(
            @MappingTarget SeriesEntity entity,
            SeriesUpsert seriesUpsert
    );
}
