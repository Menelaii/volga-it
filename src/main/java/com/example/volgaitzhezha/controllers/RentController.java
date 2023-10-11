package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.enums.RentType;
import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.models.dtos.RentDTO;
import com.example.volgaitzhezha.models.dtos.TransportDTO;
import com.example.volgaitzhezha.services.RentService;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Rent")
@RequiredArgsConstructor
public class RentController {
    private final RentService service;
    private final TransportService transportService;
    private final ModelMapper modelMapper;

    @GetMapping("/Transport")
    public ResponseEntity<List<TransportDTO>> getAllAvailable(Double latitude,
                                                              Double longitude,
                                                              Double radius,
                                                              TransportType type
    ) {
        List<TransportDTO> body = transportService.getAllAvailable(latitude, longitude, radius, type)
                        .stream()
                        .map(t -> modelMapper.map(t, TransportDTO.class))
                        .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/Transport")
    public ResponseEntity<RentDTO> getById(@PathVariable("id") Long id) {
        RentDTO rentDTO = modelMapper.map(service.getById(id), RentDTO.class);
        return ResponseEntity.ok(rentDTO);
    }

    @GetMapping("/MyHistory")
    public ResponseEntity<List<RentDTO>> getMyHistory() {
        List<RentDTO> history = service.getMyHistory()
                .stream()
                .map(rent -> modelMapper.map(rent, RentDTO.class))
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/TransportHistory/{transportId}")
    public ResponseEntity<List<RentDTO>> getTransportHistory(@PathVariable("transportId") Long transportId) {
        List<RentDTO> history = service.getHistory(transportId)
                .stream()
                .map(rent -> modelMapper.map(rent, RentDTO.class))
                .toList();

        return ResponseEntity.ok(history);
    }

    @PostMapping("/New/{transportId}")
    public ResponseEntity<Void> startRent(@PathVariable("transportId") Long transportId, RentType rentType) {
        service.startRent(transportId, rentType);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/End/{rentId}")
    public ResponseEntity<Void> endRent(@PathVariable("rentId") Long rentId,
                                        Double latitude,
                                        Double longitude
    ) {
        service.endRent(rentId, latitude, longitude);
        return ResponseEntity.ok().build();
    }
}

