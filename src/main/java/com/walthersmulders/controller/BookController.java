package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.mapstruct.dto.book.BookUpsert;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.mapstruct.dto.book.BookWithLinksUpsert;
import com.walthersmulders.service.AuthorBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/books")
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
    public BookWithLinks createWithAuthors(@Valid @RequestBody BookWithLinksUpsert bookWithLinksUpsert) {
        return authorBookService.createBookWithAuthors(bookWithLinksUpsert);
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
    public void updateBook(@PathVariable UUID id, @Valid @RequestBody BookUpsert bookUpsert) {
        authorBookService.updateBook(id, bookUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{bookID}/authors/{authorID}")
    public void addAuthorToBook(@PathVariable UUID bookID, @PathVariable UUID authorID) {
        authorBookService.addAuthorToBook(bookID, authorID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{bookID}/authors/{authorID}")
    public void removeAuthorFromBook(@PathVariable UUID bookID, @PathVariable UUID authorID) {
        authorBookService.removeAuthorFromBook(bookID, authorID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{bookID}/genres/{genreID}")
    public void updateBookGenre(@PathVariable UUID bookID, @PathVariable UUID genreID) {
        authorBookService.updateBookGenre(bookID, genreID);
    }
}
