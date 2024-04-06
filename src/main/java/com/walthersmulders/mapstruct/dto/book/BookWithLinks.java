package com.walthersmulders.mapstruct.dto.book;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.genre.book.GenreBook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BookWithLinks(
        UUID bookID,
        String title,
        String isbn,
        Integer pages,
        String plot,
        String cover,
        LocalDate datePublished,
        LocalDateTime dateAdded,
        LocalDateTime dateUpdated,
        List<Author> authors,
        GenreBook genreBook
) {
}
