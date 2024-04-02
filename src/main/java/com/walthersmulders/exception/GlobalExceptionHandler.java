package com.walthersmulders.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    protected ApiErrorHandler handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildApiErrorHandler(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(EntityExistsException.class)
    protected ApiErrorHandler handleEntityExistsException(EntityExistsException ex) {
        return buildApiErrorHandler(HttpStatus.CONFLICT, ex.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(GenericBadRequestException.class)
    protected ApiErrorHandler handleGenericBadRequestException(GenericBadRequestException ex) {
        return buildApiErrorHandler(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    protected ApiErrorHandler handleAllUncaughtException(Exception ex) {
        return buildApiErrorHandler(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong, see the logs for more information."
        );
    }


    private ApiErrorHandler buildApiErrorHandler(HttpStatus status, String message) {
        ApiErrorHandler apiErrorHandler = ApiErrorHandler.builder()
                                                         .status(status)
                                                         .statusCode(status.value())
                                                         .message(message)
                                                         .timestamp(LocalDateTime.now())
                                                         .build();

        log.error(message);

        return apiErrorHandler;
    }
}
