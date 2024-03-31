package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.author.AuthorUpsert;
import com.walthersmulders.mapstruct.dto.author.AuthorWithBooks;
import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.persistance.entity.AuthorBookEntity;
import com.walthersmulders.persistance.entity.AuthorEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    Author entityToAuthor(AuthorEntity entity);

    @Mapping(target = "books", source = "entity", qualifiedByName = "authorBookListToBookList")
    AuthorWithBooks entityToAuthorWithBooks(AuthorEntity entity);

    AuthorEntity authorUpsertToEntity(AuthorUpsert authorUpsert);

    @InheritConfiguration
    AuthorEntity authorEntityUpdateMerge(
            @MappingTarget AuthorEntity authorEntity,
            AuthorUpsert authorUpsert
    );

    @Named(value = "authorBookListToBookList")
    default List<Book> authorBookListToBookList(AuthorEntity entity) {
        BookMapperImpl bookMapper = new BookMapperImpl();

        List<Book> list = new ArrayList<>();

        entity.getBooks()
              .stream()
              .map(AuthorBookEntity::getBook)
              .forEach(book -> list.add(bookMapper.entityToBook(book)));

        return list;
    }
}
