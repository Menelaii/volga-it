package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.TransportDTO;
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
    public ResponseEntity<TransportDTO> getTransportById(@PathVariable("id") Long id) {
        Transport transport = transportService.getById(id);
        return ResponseEntity.ok(convertToDTO(transport));
    }

    @PostMapping
    public ResponseEntity<TransportDTO> addTransport(@RequestBody TransportDTO request) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(transportService.add(transport, null)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportDTO> updateTransport(@PathVariable("id") Long id,
                                                        @RequestBody TransportDTO request
    ) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(transportService.update(id, transport)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable("id") Long id) {
        transportService.delete(id);
        return ResponseEntity.ok().build();
    }

    private TransportDTO convertToDTO(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    private Transport convertToEntity(TransportDTO transportDTO) {
        return modelMapper.map(transportDTO, Transport.class);
    }
}