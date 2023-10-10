package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.models.dtos.AdminTransportRequest;
import com.example.volgaitzhezha.models.dtos.TransportRequest;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/Account")
@RequiredArgsConstructor
public class AdminTransportController {
    private final TransportService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<TransportRequest>> getAllAccounts(XPage page,
                                                                 TransportType transportType
    ) {
        List<TransportRequest> body = service.getAll(page, transportType)
                .stream()
                .map(account -> modelMapper.map(account, TransportRequest.class))
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportRequest> getTransportById(@PathVariable Long id) {
        Transport transport = service.getById(id);
        return ResponseEntity.ok(convertToDTO(transport));
    }

    @PostMapping
    public ResponseEntity<TransportRequest> addTransport(@RequestBody AdminTransportRequest request) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(service.add(transport, request.ownerId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportRequest> updateTransport(@PathVariable Long id,
                                                            @RequestBody AdminTransportRequest request
    ) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(service.update(id, transport)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    private TransportRequest convertToDTO(Transport transport) {
        return modelMapper.map(transport, TransportRequest.class);
    }

    private Transport convertToEntity(AdminTransportRequest transportRequest) {
        return modelMapper.map(transportRequest, Transport.class);
    }
}
