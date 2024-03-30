package com.walthersmulders.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericBadRequestException extends RuntimeException {
    private final String message;

    public GenericBadRequestException(String message) {
        this.message = message;
    }
}
