package com.example.volgaitzhezha.controllers.admin;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.models.dtos.AdminTransportDTO;
import com.example.volgaitzhezha.models.dtos.TransportDTO;
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
    public ResponseEntity<List<TransportDTO>> getAllAccounts(XPage page,
                                                             TransportType transportType
    ) {
        List<TransportDTO> body = service.getAll(page, transportType)
                .stream()
                .map(account -> modelMapper.map(account, TransportDTO.class))
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportDTO> getTransportById(@PathVariable Long id) {
        Transport transport = service.getById(id);
        return ResponseEntity.ok(convertToDTO(transport));
    }

    @PostMapping
    public ResponseEntity<TransportDTO> addTransport(@RequestBody AdminTransportDTO request) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(service.add(transport, request.ownerId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportDTO> updateTransport(@PathVariable Long id,
                                                        @RequestBody AdminTransportDTO request
    ) {
        Transport transport = convertToEntity(request);
        return ResponseEntity.ok(convertToDTO(service.update(id, transport)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    private TransportDTO convertToDTO(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    private Transport convertToEntity(AdminTransportDTO transportRequest) {
        return modelMapper.map(transportRequest, Transport.class);
    }
}
