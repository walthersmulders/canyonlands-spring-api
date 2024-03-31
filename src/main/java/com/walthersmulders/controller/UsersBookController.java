package com.walthersmulders.controller;

import com.walthersmulders.mapstruct.dto.usersbook.UsersBook;
import com.walthersmulders.mapstruct.dto.usersbook.UsersBookUpsert;
import com.walthersmulders.service.UsersBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
@RestController
@Validated
public class UsersBookController {
    private final UsersBookService usersBookService;

    public UsersBookController(UsersBookService usersBookService) {
        this.usersBookService = usersBookService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userID}/books/{bookID}")
    public UsersBook addBookToUserLibrary(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "bookID") UUID bookID,
            @Valid @RequestBody UsersBookUpsert usersBookUpsert
    ) {
        return usersBookService.addBookToUserLibrary(userID, bookID, usersBookUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/books/{bookID}")
    public UsersBook getUsersBook(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "bookID") UUID bookID
    ) {
        return usersBookService.getUsersBook(userID, bookID);
    }
}
