package com.walthersmulders.mapstruct.dto;

import java.util.UUID;

public record User(
        UUID userID,
        String username,
        String emailAddress,
        String firstName,
        String lastName
) {
}
