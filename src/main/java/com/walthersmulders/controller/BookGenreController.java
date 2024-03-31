package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.bookgenre.BookGenre;
import com.walthersmulders.mapstruct.dto.bookgenre.BookGenreNoID;
import com.walthersmulders.service.BookGenreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genres/books")
@RestController
@Validated
public class BookGenreController {
    private final BookGenreService bookGenreService;

    public BookGenreController(BookGenreService bookGenreService) {
        this.bookGenreService = bookGenreService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookGenre createGenre(@Valid @RequestBody BookGenreNoID bookGenreNoID) {
        return bookGenreService.create(bookGenreNoID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookGenre> getGenres() {
        return bookGenreService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public BookGenre getGenre(@PathVariable UUID id) {
        return bookGenreService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(@PathVariable UUID id, @Valid @RequestBody BookGenreNoID bookGenreNoID) {
        bookGenreService.update(id, bookGenreNoID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        bookGenreService.delete(id);
    }
}
