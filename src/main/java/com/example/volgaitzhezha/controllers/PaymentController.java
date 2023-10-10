package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @Value("cheats.hesoyam.value")
    private Double hesoyamValue;

    @PostMapping("/Hesoyam/{id}")
    public ResponseEntity<Void> hesoyam(@PathVariable("id") Long id) {
        service.deposit(id, hesoyamValue);
        return ResponseEntity.ok().build();
    }
}
