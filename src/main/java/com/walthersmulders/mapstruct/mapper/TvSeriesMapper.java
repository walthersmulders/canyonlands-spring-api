package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.GenreTvSeries;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeries;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesUpsert;
import com.walthersmulders.mapstruct.dto.tvseries.TvSeriesWithLinks;
import com.walthersmulders.persistence.entity.tvseries.TvSeriesEntity;
import com.walthersmulders.persistence.entity.tvseries.TvSeriesGenreEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TvSeriesMapper {
    TvSeriesEntity tvSeriesUpsertToEntity(TvSeriesUpsert tvSeries);

    TvSeries entityToTvSeries(TvSeriesEntity entity);

    @Mapping(target = "genres", source = "entity", qualifiedByName = "genreTvSeriesListToGenreTvSeriesList")
    TvSeriesWithLinks entityToTvSeriesWithLinks(TvSeriesEntity entity);

    @Named(value = "genreTvSeriesListToGenreTvSeriesList")
    default List<GenreTvSeries> genreTvSeriesListToGenreTvSeriesList(TvSeriesEntity entity) {
        GenreTvSeriesMapperImpl genreTvSeriesMapper = new GenreTvSeriesMapperImpl();

        List<GenreTvSeries> list = new ArrayList<>();

        entity.getTvSeriesGenres()
              .stream()
              .map(TvSeriesGenreEntity::getGenreTvSeries)
              .forEach(genre -> list.add(genreTvSeriesMapper.entityToGenreTvSeries(genre)));

        return list;
    }

    @InheritConfiguration
    TvSeriesEntity tvSeriesEntityUpdateMerge(
            @MappingTarget TvSeriesEntity tvSeriesEntity,
            TvSeriesUpsert tvSeriesUpsert
    );
}
