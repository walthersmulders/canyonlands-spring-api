package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.mapstruct.dto.book.BookAdd;
import com.walthersmulders.mapstruct.dto.book.BookWithAuthorsAdd;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.service.AuthorBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/book")
@RestController
@Validated
public class BookController {
    private final AuthorBookService authorBookService;

    public BookController(AuthorBookService authorBookService) {
        this.authorBookService = authorBookService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookWithLinks createWithAuthors(@Valid @RequestBody BookWithAuthorsAdd bookWithAuthorsAdd) {
        return authorBookService.createBookWithAuthors(bookWithAuthorsAdd);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks() {
        return authorBookService.getBooks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/links")
    public List<BookWithLinks> getBooksWithLinks() {
        return authorBookService.getBooksWithLinks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBook(@PathVariable UUID id) {
        return authorBookService.getBook(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/links")
    public BookWithLinks getBookWithLinks(@PathVariable UUID id) {
        return authorBookService.getBookWithLinks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateBook(@PathVariable UUID id, @Valid @RequestBody BookAdd bookAdd) {
        authorBookService.updateBook(id, bookAdd);
    }

    // TODO :: method to add author to book by bookID and authorID

    // TODO :: method to remove author from book by bookID and authorID (NOTE :: A book must have at least one author)

    // TODO :: method to update genre for a book by bookID and genreID (NOTE :: A book must always have a genre)
}
