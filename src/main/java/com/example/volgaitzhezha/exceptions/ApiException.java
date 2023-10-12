package com.example.volgaitzhezha.exceptions;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class ApiException {
    private String message;
    private HttpStatus status;
    private Throwable throwable;
    private LocalDateTime timeStamp;
}
