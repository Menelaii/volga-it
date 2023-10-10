package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.enums.RentType;
import com.example.volgaitzhezha.models.dtos.RentDTO;
import com.example.volgaitzhezha.services.RentService;
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
    private final ModelMapper modelMapper;

    @GetMapping("/Transport")
    public ResponseEntity<Void> getAll() {
//        GET /api/Rent/Transport
//        описание: Получение транспорта доступного для аренды по параметрам
//        параметры:
//        lat: double //Географическая широта центра круга поиска транспорта
//        long: double //Географическая долгота центра круга поиска транспорта
//        radius: double //Радиус круга поиска транспорта
//        type: “string” //Тип транспорта [Car, Bike, Scooter, All]
//        ограничения: нет

        return ResponseEntity.ok().build();
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
        service.endRent(rentId);
        return ResponseEntity.ok().build();
    }
}

