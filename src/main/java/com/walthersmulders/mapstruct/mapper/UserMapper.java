package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.User;
import com.walthersmulders.persistance.entity.UserEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity userToEntity(User user);

    User entityToUser(UserEntity userEntity);

    @InheritConfiguration
    UserEntity userEntityUpdateMerge(
            @MappingTarget UserEntity userEntity,
            String emailAddress,
            String firstName,
            String lastName
    );
}
