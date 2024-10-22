package com.walthersmulders.mapstruct.dto.users.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walthersmulders.mapstruct.dto.book.Book;

public record UsersBook(
        Book book,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String review,
        Integer rating
) {
}
