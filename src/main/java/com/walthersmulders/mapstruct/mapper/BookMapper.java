package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.mapstruct.dto.book.BookAdd;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.persistance.entity.AuthorBookEntity;
import com.walthersmulders.persistance.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    @Mapping(target = "authors", source = "entity", qualifiedByName = "authorBookListToAuthorList")
    BookWithLinks entityToBookWithLinks(BookEntity entity);

    BookEntity bookAddToEntity(BookAdd book);

    Book entityToBook(BookEntity entity);

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
