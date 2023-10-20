package com.example.volgaitzhezha.controllers.user;

import com.example.volgaitzhezha.mappers.TransportMapper;
import com.example.volgaitzhezha.models.dtos.transport.CreateTransportRequestDTO;
import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> addTransport(@RequestBody CreateTransportRequestDTO request) {
        Transport transport = mapper.map(request);
        transportService.add(transport);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransport(@PathVariable("id") Long id,
                                                        @RequestBody CreateTransportRequestDTO request
    ) {
        Transport transport = mapper.map(request);
        transportService.update(id, transport);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable("id") Long id) {
        transportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}