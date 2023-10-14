package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.AdminTransportDTO;
import com.example.volgaitzhezha.models.dtos.TransportDTO;
import com.example.volgaitzhezha.models.entities.Transport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransportMapper {
    private final ModelMapper modelMapper;
    private final AccountsMapper accountsMapper;

    public TransportDTO map(Transport transport) {
        return modelMapper.map(transport, TransportDTO.class);
    }

    public Transport map(TransportDTO dto) {
        return modelMapper.map(dto, Transport.class);
    }

    public Transport map(AdminTransportDTO dto) {
        Transport transport = modelMapper.map(dto, Transport.class);
        transport.setOwner(accountsMapper.map(dto.ownerId()));
        return transport;
    }

    public Transport map(Long id) {
        Transport transport = new Transport();
        transport.setId(id);
        return transport;
    }
}
