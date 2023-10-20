package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.transport.CreateTransportAdminRequestDTO;
import com.example.volgaitzhezha.models.dtos.transport.CreateTransportRequestDTO;
import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransportMapper {
    private final ModelMapper modelMapper;
    private final AccountsMapper accountsMapper;

    public Transport map(CreateTransportRequestDTO dto) {
        return modelMapper.map(dto, Transport.class);
    }

    public Transport map(CreateTransportAdminRequestDTO dto) {
        Transport transport = modelMapper.map(dto, Transport.class);
        transport.setOwner(accountsMapper.map(dto.ownerId()));
        return transport;
    }

    public Transport map(Long id) {
        Transport transport = new Transport();
        transport.setId(id);
        return transport;
    }

    public TransportDTO map(Transport transport) {
        TransportDTO dto = modelMapper.map(transport, TransportDTO.class);
        dto.setOwner(accountsMapper.map(transport.getOwner()));
        return dto;
    }
}
