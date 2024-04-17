package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.users.series.UsersSeries;
import com.walthersmulders.persistence.entity.users.series.UsersSeriesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersSeriesMapper {
    UsersSeries entityToUsersSeries(UsersSeriesEntity entity);
}
