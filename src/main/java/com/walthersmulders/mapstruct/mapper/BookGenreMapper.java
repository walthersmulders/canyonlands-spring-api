package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.BookGenre;
import com.walthersmulders.mapstruct.dto.BookGenreNoID;
import com.walthersmulders.persistance.entity.BookGenreEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookGenreMapper {
    BookGenre entityToBookGenre(BookGenreEntity entity);

    BookGenreEntity bookGenreNoIDToEntity(BookGenreNoID bookGenreNoID);

    @InheritConfiguration
    BookGenreEntity bookGenreEntityUpdateMerge(
            @MappingTarget BookGenreEntity entity,
            BookGenreNoID bookGenreNoID
    );
}
