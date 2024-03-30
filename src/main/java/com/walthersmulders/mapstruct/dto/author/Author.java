package com.walthersmulders.mapstruct.dto.author;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

public record Author(
        UUID authorID,
        String firstName,
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String additionalName
) {
}
