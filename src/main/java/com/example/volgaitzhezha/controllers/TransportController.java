package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.TransportRequest;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Transport")
@RequiredArgsConstructor
public class TransportController {
    private final TransportService transportService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TransportRequest> getTransportById(@PathVariable Long id) {
        Transport transport = transportService.getById(id);
        return ResponseEntity.ok(convertToDTO(transport));
    }

    @PostMapping
    public ResponseEntity<TransportRequest> addTransport(@RequestBody TransportRequest request) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(transportService.add(transport, null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportRequest> updateTransport(@PathVariable Long id, @RequestBody TransportRequest request) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(transportService.update(id, transport)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        transportService.delete(id);
        return ResponseEntity.ok().build();
    }

    private TransportRequest convertToDTO(Transport transport) {
        return modelMapper.map(transport, TransportRequest.class);
    }

    private Transport convertToEntity(TransportRequest transportRequest) {
        return modelMapper.map(transportRequest, Transport.class);
    }
}