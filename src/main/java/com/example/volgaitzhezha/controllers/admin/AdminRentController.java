package com.example.volgaitzhezha.controllers.admin;

import com.example.volgaitzhezha.models.dtos.AdminRentDTO;
import com.example.volgaitzhezha.models.dtos.RentDTO;
import com.example.volgaitzhezha.models.entities.Rent;
import com.example.volgaitzhezha.services.RentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin")
@RequiredArgsConstructor
public class AdminRentController {
    private final RentService service;
    private final ModelMapper modelMapper;

    @GetMapping("/Rent/{id}")
    public ResponseEntity<RentDTO> getById(@PathVariable("id") Long id) {
        RentDTO rentDTO = modelMapper.map(service.getById(id), RentDTO.class);
        return ResponseEntity.ok(rentDTO);
    }

    @GetMapping("/UserHistory/{userId}")
    public ResponseEntity<List<RentDTO>> getUserHistory(@PathVariable("userId") Long userId) {
        List<RentDTO> history = service.getUserHistory(userId)
                .stream()
                .map(rent -> modelMapper.map(rent, RentDTO.class))
                .toList();

        return ResponseEntity.ok(history);
    }

    @GetMapping("/TransportHistory/{transportId}")
    public ResponseEntity<List<RentDTO>> getTransportHistory(@PathVariable("transportId") Long transportId) {
        List<RentDTO> history = service.getTransportHistory(transportId)
                .stream()
                .map(rent -> modelMapper.map(rent, RentDTO.class))
                .toList();

        return ResponseEntity.ok(history);
    }

    @PostMapping("/Rent")
    public ResponseEntity<Void> createRent(@RequestBody AdminRentDTO rentDTO) {
        Rent rent = modelMapper.map(rentDTO, Rent.class);
        service.save(rent, rentDTO.transportId(), rentDTO.userId());
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

    @PutMapping("/Rent/{id}")
    public ResponseEntity<Void> updateRent(@PathVariable("id") Long id,
                                           @RequestBody AdminRentDTO rentDTO
    ) {
        Rent rent = modelMapper.map(rentDTO, Rent.class);
        service.update(id, rent, rentDTO.transportId(), rentDTO.userId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
