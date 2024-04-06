package com.walthersmulders.service.user;

import com.walthersmulders.mapstruct.dto.user.User;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.persistence.entity.user.UserEntity;
import com.walthersmulders.persistence.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper     userMapper;

    public RegistrationService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void register(Jwt jwt) {
        log.info("Registering user with clientId: {}", jwt.getClaimAsString("sub"));

        User user = new User(
            UUID.fromString(jwt.getClaimAsString("sub")),
            jwt.getClaimAsString("preferred_username"),
            jwt.getClaimAsString("email"),
            jwt.getClaimAsString("given_name"),
            jwt.getClaimAsString("family_name")
        );

        UserEntity userEntity = userMapper.userToEntity(user);

        userRepository.save(userEntity);

        log.info("User registered successfully");
    }
}
