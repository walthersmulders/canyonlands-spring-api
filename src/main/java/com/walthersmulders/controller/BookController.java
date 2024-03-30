package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.book.BookWithAuthorsAdd;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.service.AuthorBookService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BookWithLinks createWithAuthors(@RequestBody BookWithAuthorsAdd bookWithAuthorsAdd) {
        return authorBookService.createBookWithAuthors(bookWithAuthorsAdd);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookWithLinks> getBooks() {
        return authorBookService.getBooks();
    }
}
