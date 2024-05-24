package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.series.Season;
import com.walthersmulders.mapstruct.dto.series.SeasonUpsert;
import com.walthersmulders.persistence.entity.series.SeasonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeasonMapper {
    SeasonEntity seasonUpsertToEntity(SeasonUpsert seasonUpsert);

    Season entityToSeason(SeasonEntity entity);
}
