package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.GenreBook;
import com.walthersmulders.mapstruct.dto.GenreBookNoID;
import com.walthersmulders.service.GenreBookService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/genre/book")
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
    public GenreBook createGenre(@RequestBody GenreBookNoID genreBookNoID) {
        return genreBookService.create(genreBookNoID);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GenreBook> getGenres() {
        return genreBookService.getGenres();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public GenreBook getGenre(@PathVariable UUID id) {
        return genreBookService.getGenre(id);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateGenre(@PathVariable UUID id, @RequestBody GenreBookNoID genreBookNoID) {
        genreBookService.update(id, genreBookNoID);
    }

    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        genreBookService.delete(id);
    }
}
