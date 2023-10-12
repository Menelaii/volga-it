package com.example.volgaitzhezha.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException e) {
        return ResponseEntity.badRequest().body(
                new ApiException(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        e,
                        LocalDateTime.now()
                )
        );
    }
}
