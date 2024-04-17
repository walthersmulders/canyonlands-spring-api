package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.series.GenreSeries;
import com.walthersmulders.mapstruct.dto.genre.series.GenreSeriesUpsert;
import com.walthersmulders.persistence.entity.genre.GenreSeriesEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreSeriesMapper {
    GenreSeries entityToGenreSeries(GenreSeriesEntity entity);

    GenreSeriesEntity genreSeriesUpsertToEntity(GenreSeriesUpsert genreSeriesUpsert);

    @InheritConfiguration
    GenreSeriesEntity genreSeriesEntityUpdateMerge(
            @MappingTarget GenreSeriesEntity entity,
            GenreSeriesUpsert genreSeriesUpsert
    );
}
