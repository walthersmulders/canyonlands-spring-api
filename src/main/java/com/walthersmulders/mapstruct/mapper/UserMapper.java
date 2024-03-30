package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.User;
import com.walthersmulders.persistance.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity userToEntity(User user);
}
