package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.mappers.TransportMapper;
import com.example.volgaitzhezha.models.dtos.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Transport")
@RequiredArgsConstructor
public class TransportController {
    private final TransportService transportService;
    private final TransportMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<TransportDTO> getTransportById(@PathVariable("id") Long id) {
        Transport transport = transportService.getById(id);
        return ResponseEntity.ok(mapper.map(transport));
    }

    @PostMapping
    public ResponseEntity<TransportDTO> addTransport(@RequestBody TransportDTO request) {
        Transport transport = mapper.map(request);
        return ResponseEntity.ok(mapper.map(transportService.add(transport)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportDTO> updateTransport(@PathVariable("id") Long id,
                                                        @RequestBody TransportDTO request
    ) {
        Transport transport = mapper.map(request);
        return ResponseEntity.ok(mapper.map(transportService.update(id, transport)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable("id") Long id) {
        transportService.delete(id);
        return ResponseEntity.ok().build();
    }
}