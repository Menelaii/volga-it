package com.example.volgaitzhezha.controllers.admin;

import com.example.volgaitzhezha.mappers.RentMapper;
import com.example.volgaitzhezha.models.dtos.rent.*;
import com.example.volgaitzhezha.models.entities.Rent;
import com.example.volgaitzhezha.services.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin")
@RequiredArgsConstructor
public class AdminRentController {
    private final RentService service;
    private final RentMapper mapper;

    @GetMapping("/Rent/{id}")
    public ResponseEntity<RentFullDTO> getById(@PathVariable("id") Long id) {
        RentFullDTO rentDTO = mapper.map(service.getById(id));
        return ResponseEntity.ok(rentDTO);
    }

    @GetMapping("/UserHistory/{userId}")
    public ResponseEntity<List<UserHistoryRentDTO>> getUserHistory(@PathVariable("userId") Long userId) {
        List<UserHistoryRentDTO> history = service.getUserHistory(userId)
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

    @PostMapping("/Rent")
    public ResponseEntity<Void> createRent(@RequestBody CreateRentAdminRequestDTO rentDTO) {
        Rent rent = mapper.map(rentDTO);
        service.save(rent, rentDTO.transportId(), rentDTO.userId());
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/End/{rentId}")
    public ResponseEntity<Void> endRent(@PathVariable("rentId") Long rentId,
                                        Double latitude,
                                        Double longitude
    ) {
        service.endRent(rentId, latitude, longitude);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/Rent/{id}")
    public ResponseEntity<Void> updateRent(@PathVariable("id") Long id,
                                           @RequestBody CreateRentAdminRequestDTO rentDTO
    ) {
        Rent rent = mapper.map(rentDTO);
        service.update(id, rent, rentDTO.transportId(), rentDTO.userId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
