package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

//todo owner
@RestController
@RequestMapping("/api/Transport")
@RequiredArgsConstructor
public class TransportController {
    private final TransportService transportService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public TransportDTO getTransportById(@PathVariable Long id) {
        Transport transport = transportService.getById(id);
        return convertToDTO(transport);
    }

    @PostMapping
    public TransportDTO addTransport(@RequestBody TransportDTO request) {
        Transport transport = convertToEntity(request);
        return convertToDTO(transportService.add(transport));
    }

    @PutMapping("/{id}")
    public TransportDTO updateTransport(@PathVariable Long id, @RequestBody TransportDTO request) {
        Transport transport = convertToEntity(request);
        return convertToDTO(transportService.update(id, transport));
    }

    @DeleteMapping("/{id}")
    public void deleteTransport(@PathVariable Long id) {
        transportService.delete(id);
    }

    private TransportDTO convertToDTO(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    private Transport convertToEntity(TransportDTO transportDTO) {
        return modelMapper.map(transportDTO, Transport.class);
    }
}