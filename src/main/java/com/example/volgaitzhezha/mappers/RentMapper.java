package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.rent.*;
import com.example.volgaitzhezha.models.entities.Rent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentMapper {
    private final ModelMapper modelMapper;
    private final AccountsMapper accountsMapper;
    private final TransportMapper transportMapper;

    public RentFullDTO map(Rent rent) {
        RentFullDTO dto = modelMapper.map(rent, RentFullDTO.class);
        dto.setRenter(accountsMapper.map(rent.getRenter()));
        dto.setTransport(transportMapper.map(rent.getTransport()));

        return dto;
    }

    public Rent map(CreateRentAdminRequestDTO dto) {
        Rent rent = modelMapper.map(dto, Rent.class);
        rent.setTransport(transportMapper.map(dto.transportId()));
        rent.setRenter(accountsMapper.map(dto.userId()));
        return rent;
    }

    public UserHistoryRentDTO mapUserHistory(Rent rent) {
        UserHistoryRentDTO dto = modelMapper.map(rent, UserHistoryRentDTO.class);
        dto.setTransport(transportMapper.map(rent.getTransport()));

        return dto;
    }

    public TransportHistoryRentDTO mapTransportHistory(Rent rent) {
        TransportHistoryRentDTO dto = modelMapper.map(rent, TransportHistoryRentDTO.class);
        dto.setRenter(accountsMapper.map(rent.getRenter()));

        return dto;
    }
}
