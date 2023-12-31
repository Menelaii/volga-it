package com.example.volgaitzhezha.controllers.user;

import com.example.volgaitzhezha.enums.RentType;
import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.mappers.RentMapper;
import com.example.volgaitzhezha.mappers.TransportMapper;
import com.example.volgaitzhezha.models.dtos.rent.RentFullDTO;
import com.example.volgaitzhezha.models.dtos.rent.TransportHistoryRentDTO;
import com.example.volgaitzhezha.models.dtos.rent.UserHistoryRentDTO;
import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import com.example.volgaitzhezha.services.RentService;
import com.example.volgaitzhezha.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Rent")
@RequiredArgsConstructor
public class RentController {
    private final RentService service;
    private final RentMapper mapper;
    private final TransportMapper transportMapper;
    private final TransportService transportService;

    @GetMapping("/Transport")
    public ResponseEntity<List<TransportDTO>> getAllAvailable(Double latitude,
                                                              Double longitude,
                                                              Double radius,
                                                              TransportType type
    ) {
        List<TransportDTO> body = transportService.getAllAvailable(latitude, longitude, radius, type)
                        .stream()
                        .map(transportMapper::map)
                        .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentFullDTO> getById(@PathVariable("id") Long id) {
        RentFullDTO rentDTO = mapper.map(service.getById(id));
        return ResponseEntity.ok(rentDTO);
    }

    @GetMapping("/MyHistory")
    public ResponseEntity<List<UserHistoryRentDTO>> getMyHistory() {
        List<UserHistoryRentDTO> history = service.getMyHistory()
                .stream()
                .map(mapper::mapUserHistory)
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/TransportHistory/{transportId}")
    public ResponseEntity<List<TransportHistoryRentDTO>> getTransportHistory(@PathVariable("transportId") Long transportId) {
        List<TransportHistoryRentDTO> history = service.getTransportHistory(transportId)
                .stream()
                .map(mapper::mapTransportHistory)
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

