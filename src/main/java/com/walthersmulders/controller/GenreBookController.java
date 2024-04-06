package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.genre.book.GenreBook;
import com.walthersmulders.mapstruct.dto.genre.book.GenreBookUpsert;
import com.walthersmulders.service.GenreBookService;
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
public class GenreBookController {
    private final GenreBookService genreBookService;

    public GenreBookController(GenreBookService genreBookService) {
        this.genreBookService = genreBookService;
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GenreBook create(@Valid @RequestBody GenreBookUpsert genreBookUpsert) {
        return genreBookService.create(genreBookUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreBook> getAll() {
        return genreBookService.getGenres();
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreBook getGenre(@PathVariable UUID id) {
        return genreBookService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(@PathVariable UUID id, @Valid @RequestBody GenreBookUpsert genreBookUpsert) {
        genreBookService.update(id, genreBookUpsert);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        genreBookService.delete(id);
    }
}
