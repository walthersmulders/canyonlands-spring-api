package com.walthersmulders.controller.users;

import com.walthersmulders.mapstruct.dto.users.album.UsersAlbum;
import com.walthersmulders.mapstruct.dto.users.album.UsersAlbumUpsert;
import com.walthersmulders.service.users.UsersAlbumService;
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
public class UsersAlbumController {
    private final UsersAlbumService usersAlbumService;

    public UsersAlbumController(UsersAlbumService usersAlbumService) {
        this.usersAlbumService = usersAlbumService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{userID}/albums/{albumID}")
    public UsersAlbum addAlbumToUserLibrary(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "albumID") UUID albumID,
            @Valid @RequestBody UsersAlbumUpsert usersAlbumUpsert
    ) {
        return usersAlbumService.addAlbumToUserLibrary(userID, albumID, usersAlbumUpsert);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/albums/{albumID}")
    public UsersAlbum getUsersAlbum(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "albumID") UUID albumID
    ) {
        return usersAlbumService.getUsersAlbum(userID, albumID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userID}/albums")
    public List<UsersAlbum> getAllUserAlbums(@PathVariable(name = "userID") UUID userID) {
        return usersAlbumService.getAllUserAlbums(userID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userID}/albums/{albumID}")
    public void delete(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "albumID") UUID albumID
    ) {
        usersAlbumService.removeAlbumFromUserLibrary(userID, albumID);
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{userID}/albums/{albumID}")
    public void update(
            @PathVariable(name = "userID") UUID userID,
            @PathVariable(name = "albumID") UUID albumID,
            @Valid @RequestBody UsersAlbumUpsert usersAlbumUpsert
    ) {
        usersAlbumService.update(userID, albumID, usersAlbumUpsert);
    }
}
