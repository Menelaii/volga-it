package com.example.volgaitzhezha.controllers.admin;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.mappers.TransportMapper;
import com.example.volgaitzhezha.models.dtos.transport.CreateTransportAdminRequestDTO;
import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/Transport")
@RequiredArgsConstructor
public class AdminTransportController {
    private final TransportService service;
    private final TransportMapper mapper;

    @GetMapping
    public ResponseEntity<List<TransportDTO>> getAll(XPage page,
                                                     TransportType transportType
    ) {
        List<TransportDTO> body = service.getAll(page, transportType)
                .stream()
                .map(mapper::map)
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportDTO> getTransportById(@PathVariable Long id) {
        Transport transport = service.getById(id);
        return ResponseEntity.ok(mapper.map(transport));
    }

    @PostMapping
    public ResponseEntity<Void> addTransport(@RequestBody CreateTransportAdminRequestDTO request) {
        Transport transport = mapper.map(request);
        service.add(transport);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransport(@PathVariable Long id,
                                                @RequestBody CreateTransportAdminRequestDTO request
    ) {
        Transport transport = mapper.map(request);
        service.update(id, transport);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
