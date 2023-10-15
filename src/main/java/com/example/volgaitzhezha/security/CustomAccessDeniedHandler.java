package com.example.volgaitzhezha.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//todo не работает
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex
    ) throws IOException, ServletException {
        HttpStatus status;
        String message;
        if (request.getRequestURI().startsWith("/api/Admin")) {
            status = HttpStatus.FORBIDDEN;
            message = "Недостаточно прав";
        } else {
            status = HttpStatus.UNAUTHORIZED;
            message = "Войдите в аккаунт";
        }

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("status", status);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), body);

        response.sendError(status.value());
    }
}