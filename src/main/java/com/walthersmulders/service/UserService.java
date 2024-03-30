package com.walthersmulders.service;

import com.walthersmulders.mapstruct.dto.User;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.persistance.entity.UserEntity;
import com.walthersmulders.persistance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
}
