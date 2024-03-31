package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.author.AuthorNoID;
import com.walthersmulders.mapstruct.dto.author.AuthorWithBooks;
import com.walthersmulders.service.AuthorBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/author")
@RestController
@Validated
public class AuthorController {
    private final AuthorBookService authorBookService;

    public AuthorController(AuthorBookService authorBookService) {
        this.authorBookService = authorBookService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Author create(@Valid @RequestBody AuthorNoID authorNoID) {
        return authorBookService.createAuthor(authorNoID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Author> getAuthors() {
        return authorBookService.getAuthors();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/books")
    public List<AuthorWithBooks> getAuthorsWithBooks() {
        return authorBookService.getAuthorsWithBooks();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Author getAuthor(@PathVariable UUID id) {
        return authorBookService.getAuthor(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/books")
    public AuthorWithBooks getAuthorWithBooks(@PathVariable UUID id) {
        return authorBookService.getAuthorWithBooks(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateAuthor(@PathVariable UUID id, @Valid @RequestBody AuthorNoID authorNoID) {
        authorBookService.updateAuthor(id, authorNoID);
    }

    // TODO :: method to add book to author by authorID

    // TODO :: method to remove book from author by authorID
}