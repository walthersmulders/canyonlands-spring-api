package com.walthersmulders.controller.users;

import com.walthersmulders.mapstruct.dto.users.movie.UsersMovie;
import com.walthersmulders.mapstruct.dto.users.movie.UsersMovieUpsert;
import com.walthersmulders.service.UsersMovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
@Validated
public class UsersMovieController {
    private final UsersMovieService usersMovieService;

    public UsersMovieController(UsersMovieService usersMovieService) {
        this.usersMovieService = usersMovieService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userID}/movies/{movieID}")
    public UsersMovie addMovieToUserLibrary(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "movieID") UUID movieID,
            @Valid @RequestBody UsersMovieUpsert usersMovieUpsert
    ) {
        return usersMovieService.addMovieToUserLibrary(userID, movieID, usersMovieUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/movies/{movieID}")
    public UsersMovie getUsersMovie(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "movieID") UUID movieID
    ) {
        return usersMovieService.getUsersMovie(userID, movieID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/movies")
    public List<UsersMovie> getUserMovies(@PathVariable(name = "userID") UUID userID) {
        return usersMovieService.getUserMovies(userID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userID}/movies/{movieID}")
    public void delete(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "movieID") UUID movieID
    ) {
        usersMovieService.removeMovieFromUserLibrary(userID, movieID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{userID}/movies/{movieID}")
    public void update(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "movieID") UUID movieID,
            @Valid @RequestBody UsersMovieUpsert usersMovieUpsert
    ) {
        usersMovieService.update(userID, movieID, usersMovieUpsert);
    }
}
