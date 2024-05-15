package com.walthersmulders.mapstruct.dto.artist;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public record Artist(
        UUID artistID,
        String firstName,
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String additionalName
) {
}
