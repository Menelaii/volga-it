package com.example.volgaitzhezha.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiException {
    private String message;
    private HttpStatus status;
    private LocalDateTime timeStamp;
}
