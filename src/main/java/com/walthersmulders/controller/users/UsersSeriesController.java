package com.walthersmulders.controller.users;

import com.walthersmulders.mapstruct.dto.users.series.UsersSeries;
import com.walthersmulders.mapstruct.dto.users.series.UsersSeriesUpsert;
import com.walthersmulders.service.users.UsersSeriesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// TODO :: review and impl
@RequestMapping("/users")
@RestController
@Validated
public class UsersSeriesController {
    private final UsersSeriesService usersSeriesService;

    public UsersSeriesController(UsersSeriesService usersSeriesService) {
        this.usersSeriesService = usersSeriesService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userID}/series/{seriesID}")
    public UsersSeries addSeriesToUserLibrary(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "seriesID") UUID seriesID,
            @Valid @RequestBody UsersSeriesUpsert usersSeriesUpsert
    ) {
        return usersSeriesService.addSeriesToUserLibrary(userID, seriesID, usersSeriesUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/series/{seriesID}")
    public UsersSeries getUsersSeries(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "seriesID") UUID seriesID
    ) {
        return usersSeriesService.getUsersSeries(userID, seriesID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/series")
    public List<UsersSeries> getUserSeries(@PathVariable(name = "userID") UUID userID) {
        return usersSeriesService.getUserSeries(userID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userID}/series/{seriesID}")
    public void delete(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "seriesID") UUID seriesID
    ) {
        usersSeriesService.removeSeriesFromUserLibrary(userID, seriesID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{userID}/series/{seriesID}")
    public void update(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "seriesID") UUID seriesID,
            @Valid @RequestBody UsersSeriesUpsert usersSeriesUpsert
    ) {
        usersSeriesService.update(userID, seriesID, usersSeriesUpsert);
    }
}
