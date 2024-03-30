package com.walthersmulders.service;

import com.walthersmulders.mapstruct.dto.User;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.persistance.entity.UserEntity;
import com.walthersmulders.persistance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper     userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<User> getUsers() {
        log.info("Getting all users");

        List<UserEntity> users = userRepository.findAll();

        log.info("Found {} users", users.size());

        return users.isEmpty() ? Collections.emptyList()
                               : users.stream()
                                      .map(userMapper::entityToUser)
                                      .toList();
    }

    public User updateUser(UUID id, Jwt jwt) {
        log.info("Updating user with id: {}", id);
        log.info("Check if incoming user id is the same as the user id in the JWT");

        if (!id.equals(UUID.fromString(jwt.getClaimAsString("sub")))) {
            log.error("User id in the path variable does not match the user id in the JWT");
            // TODO :: Custom bad request exception handler
            return null;
        }

        log.info("User id in the path variable matches the user id in the JWT");

        Optional<UserEntity> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            log.error("User with id {} not found", id);
            // TODO :: custom entity not found exception
            return null;
        }

        String emailAddress = jwt.getClaimAsString("email");
        String firstName    = jwt.getClaimAsString("given_name");
        String lastName     = jwt.getClaimAsString("family_name");

        UserEntity updatedUser = userMapper.userEntityUpdateMerge(
                existingUser.get(),
                emailAddress,
                firstName,
                lastName
        );

        userRepository.save(updatedUser);

        log.info("User updated successfully");

        return userMapper.entityToUser(updatedUser);
    }
}
