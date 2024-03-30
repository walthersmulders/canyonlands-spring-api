package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.GenreBook;
import com.walthersmulders.mapstruct.dto.GenreBookNoID;
import com.walthersmulders.persistance.entity.GenreBookEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreBookMapper {
    GenreBook entityToGenreBook(GenreBookEntity entity);

    GenreBookEntity genreBookNoIDToEntity(GenreBookNoID genreBookNoID);

    @InheritConfiguration
    GenreBookEntity genreBookEntityUpdateMerge(
            @MappingTarget GenreBookEntity entity,
            GenreBookNoID genreBookNoID
    );
}
