package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.music.GenreMusic;
import com.walthersmulders.mapstruct.dto.genre.music.GenreMusicUpsert;
import com.walthersmulders.persistence.entity.genre.GenreMusicEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreMusicMapper {
    GenreMusic entityToGenreMusic(GenreMusicEntity entity);

    GenreMusicEntity genreMusicUpsertToEntity(GenreMusicUpsert genreMusicUpsert);

    @InheritConfiguration
    GenreMusicEntity genreMusicEntityUpdateMerge(
            @MappingTarget GenreMusicEntity entity,
            GenreMusicUpsert genreMusicUpsert
    );
}
