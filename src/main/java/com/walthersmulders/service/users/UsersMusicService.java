package com.walthersmulders.service.users;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.users.album.UsersAlbum;
import com.walthersmulders.mapstruct.dto.users.album.UsersMusicUpsert;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.mapstruct.mapper.UsersMusicMapper;
import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import com.walthersmulders.persistence.entity.users.music.UsersMusicEntity;
import com.walthersmulders.persistence.entity.users.music.UsersMusicID;
import com.walthersmulders.persistence.repository.music.AlbumRepository;
import com.walthersmulders.persistence.repository.user.UserRepository;
import com.walthersmulders.persistence.repository.users.UsersMusicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UsersMusicService {
    private static final String ALBUM_ID = "albumID";
    private static final String ALBUM    = "Album";
    private static final String USER_ID  = "userID";

    private final UserRepository       userRepository;
    private final UserMapper           userMapper;
    private final AlbumRepository      albumRepository;
    private final UsersMusicRepository usersMusicRepository;
    private final UsersMusicMapper     usersMusicMapper;

    public UsersMusicService(
            UserRepository userRepository,
            UserMapper userMapper,
            AlbumRepository albumRepository,
            UsersMusicRepository usersMusicRepository,
            UsersMusicMapper usersMusicMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.albumRepository = albumRepository;
        this.usersMusicRepository = usersMusicRepository;
        this.usersMusicMapper = usersMusicMapper;
    }

    @Transactional
    public UsersAlbum addAlbumToUserLibrary(UUID userID, UUID albumID, UsersMusicUpsert usersMusicUpsert) {
        log.info("Adding album to user library for userID: {} and albumID: {}", userID, albumID);

        Optional<UserEntity> userWithAlbums = userRepository.fetchWithAlbums(userID);

        if (userWithAlbums.isEmpty()) {
            log.error("User with userID: {} not found", userID);

            throw new EntityNotFoundException("User", Map.of(USER_ID, userID.toString()));
        }

        UsersMusicEntity existingAlbum = userWithAlbums.get()
                                                       .getAlbums()
                                                       .stream()
                                                       .filter(album -> album.getAlbum().getAlbumID().equals(albumID))
                                                       .findFirst()
                                                       .orElse(null);

        if (existingAlbum != null) {
            log.error("Album with albumID: {} already exists in user's library", albumID);

            throw new EntityExistsException(ALBUM, Map.of(ALBUM_ID, albumID.toString()));
        }

        log.info("Album with albumID: {} not found in user's library. Adding album to library", albumID);

        AlbumEntity album = albumRepository.findById(albumID)
                                           .orElseThrow(() -> new EntityNotFoundException(
                                                   ALBUM,
                                                   Map.of(ALBUM_ID, albumID.toString())
                                           ));

        if (usersMusicUpsert.review() != null) {
            userWithAlbums.get().addAlbumToUserLibrary(
                    album,
                    usersMusicUpsert.rating(),
                    usersMusicUpsert.review()
            );

            log.info("Album with albumID: {} added to user's library with rating and review", albumID);

            return userMapper.entityToUsersAlbum(album, usersMusicUpsert.rating(), usersMusicUpsert.review());
        } else {
            userWithAlbums.get().addAlbumToUserLibrary(album, usersMusicUpsert.rating());

            log.info("Album with albumID: {} added to user's library with rating", albumID);

            return userMapper.entityToUsersAlbum(album, usersMusicUpsert.rating());
        }
    }

    @Transactional(readOnly = true)
    public UsersAlbum getUsersAlbum(UUID userID, UUID albumID) {
        log.info("Fetching album with albumID: {} for user with userID: {}", albumID, userID);

        Optional<UsersMusicEntity> usersAlbum = usersMusicRepository.fetchUsersAlbum(userID, albumID);

        if (usersAlbum.isEmpty()) {
            log.error("Combination with userID: {} and albumID: {} not found", userID, albumID);

            throw new EntityNotFoundException("UsersAlbum", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(ALBUM_ID, albumID.toString())
            ));
        }

        log.info("Combination with userID: {} and albumID: {} found", userID, albumID);

        return usersMusicMapper.entityToUsersMusic(usersAlbum.get());
    }

    public List<UsersAlbum> getAllUserAlbums(UUID userID) {
        log.info("Fetching all albums for user with userID: {}", userID);

        List<UsersMusicEntity> usersAlbums = usersMusicRepository.fetchAllUsersAlbums(userID);

        log.info("Fetched all albums for user with userID: {}", userID);

        return usersAlbums.stream().map(usersMusicMapper::entityToUsersMusic).toList();
    }

    public void removeAlbumFromUserLibrary(UUID userID, UUID albumID) {
        log.info("Deleting album with albumID: {} for user with userID: {}", albumID, userID);

        usersMusicRepository.deleteById(new UsersMusicID(userID, albumID));

        log.info("Deleted album with albumID: {} for user with userID: {}", albumID, userID);
    }

    public void update(UUID userID, UUID albumID, UsersMusicUpsert usersMusicUpsert) {
        log.info("Updating album with albumID: {} for user with userID: {}", albumID, userID);

        Optional<UsersMusicEntity> existingUsersAlbum = usersMusicRepository.findById(
                new UsersMusicID(userID, albumID)
        );

        if (existingUsersAlbum.isEmpty()) {
            log.error("Combination with userID: {} and albumID: {} not found", userID, albumID);

            throw new EntityNotFoundException("UsersAlbum", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(ALBUM_ID, albumID.toString())
            ));
        }

        existingUsersAlbum.get().setReview(usersMusicUpsert.review());
        existingUsersAlbum.get().setRating(usersMusicUpsert.rating());

        usersMusicRepository.save(existingUsersAlbum.get());

        log.info("Updated album with albumID: {} for user with userID: {}", albumID, userID);
    }
}
