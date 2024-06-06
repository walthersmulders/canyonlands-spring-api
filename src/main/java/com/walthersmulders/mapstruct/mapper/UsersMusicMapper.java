package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.users.album.UsersAlbum;
import com.walthersmulders.persistence.entity.users.music.UsersMusicEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMusicMapper {
    UsersAlbum entityToUsersMusic(UsersMusicEntity entity);
}
