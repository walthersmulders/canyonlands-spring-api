package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.GenreTvSeries;
import com.walthersmulders.mapstruct.dto.genre.GenreTvSeriesUpsert;
import com.walthersmulders.persistence.entity.genre.GenreTvSeriesEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreTvSeriesMapper {
    GenreTvSeries entityToGenreTvSeries(GenreTvSeriesEntity genreTvSeriesEntity);

    GenreTvSeriesEntity genreTvSeriesUpsertToEntity(GenreTvSeriesUpsert genreTvSeriesUpsert);

    @InheritConfiguration
    GenreTvSeriesEntity genreTvSeriesEntityUpdateMerge(
            @MappingTarget GenreTvSeriesEntity entity,
            GenreTvSeriesUpsert genreTvSeriesUpsert
    );
}
