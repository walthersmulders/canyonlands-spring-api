package com.walthersmulders.mapstruct.dto.author;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.book.Book;

import java.util.List;
import java.util.UUID;

public record AuthorWithBooks(
        UUID authorID,
        String firstName,
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String additionalName,
        List<Book> books
) {
}
