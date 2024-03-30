package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.author.AuthorNoID;
import com.walthersmulders.mapstruct.dto.author.AuthorWithBooks;
import com.walthersmulders.service.AuthorBookService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Author create(@RequestBody AuthorNoID authorNoID) {
        return authorBookService.createAuthor(authorNoID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AuthorWithBooks> getAuthors() {
        return authorBookService.getAuthors();
    }
}
