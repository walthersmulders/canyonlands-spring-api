package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.genre.book.GenreBook;
import com.walthersmulders.mapstruct.dto.genre.book.GenreBookUpsert;
import com.walthersmulders.persistence.entity.genre.GenreBookEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenreBookMapper {
    GenreBook entityToGenreBook(GenreBookEntity entity);

    GenreBookEntity genreBookUpsertToEntity(GenreBookUpsert genreBookUpsert);

    @InheritConfiguration
    GenreBookEntity genreBookEntityUpdateMerge(
            @MappingTarget GenreBookEntity entity,
            GenreBookUpsert genreBookUpsert
    );
}
