package com.example.volgaitzhezha.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class EntityNotFoundExceptionHandler {
    private static final String DEFAULT_MESSAGE = "Запись не найдена";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiException> handleApiRequestException(EntityNotFoundException e) {
        return new ResponseEntity<>(
                new ApiException(e.getMessage() != null ? e.getMessage() : DEFAULT_MESSAGE,
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
