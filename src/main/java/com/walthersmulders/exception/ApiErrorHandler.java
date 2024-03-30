package com.walthersmulders.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorHandler {
    private HttpStatus status;
    private int        statusCode;
    private String     message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CustomValidationError> validationErrors = new ArrayList<>();

    public void addCustomValidationErrors(String field, String message) {
        validationErrors.add(new CustomValidationError(field, message));
    }
}

record CustomValidationError(
        String field,
        String message
) {}
