package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.AdminRentDTO;
import com.example.volgaitzhezha.models.dtos.RentDTO;
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

    public RentDTO map(Rent rent) {
        RentDTO dto = modelMapper.map(rent, RentDTO.class);
        dto.setRenter(accountsMapper.map(rent.getRenter()));
        dto.setTransport(transportMapper.map(rent.getTransport()));
        return dto;
    }

    public Rent map(AdminRentDTO dto) {
        Rent rent = modelMapper.map(dto, Rent.class);
        rent.setTransport(transportMapper.map(dto.transportId()));
        rent.setRenter(accountsMapper.map(dto.userId()));
        return rent;
    }
}
