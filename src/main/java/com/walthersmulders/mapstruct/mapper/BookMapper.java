package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.mapstruct.dto.book.BookUpsert;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.persistance.entity.AuthorBookEntity;
import com.walthersmulders.persistance.entity.BookEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    @Mapping(target = "authors", source = "entity", qualifiedByName = "authorBookListToAuthorList")
    BookWithLinks entityToBookWithLinks(BookEntity entity);

    BookEntity bookUpsertToEntity(BookUpsert bookUpsert);

    Book entityToBook(BookEntity entity);

    @InheritConfiguration
    BookEntity bookEntityUpdateMerge(
            @MappingTarget BookEntity entity,
            BookUpsert bookUpsert
    );

    @Named(value = "authorBookListToAuthorList")
    default List<Author> authorBookListToAuthorList(BookEntity entity) {
        AuthorMapperImpl authorMapper = new AuthorMapperImpl();

        List<Author> list = new ArrayList<>();

        entity.getAuthors()
              .stream()
              .map(AuthorBookEntity::getAuthor)
              .forEach(author -> list.add(authorMapper.entityToAuthor(author)));

        return list;
    }
}
