package com.example.volgaitzhezha.models.dtos.transport;

import com.example.volgaitzhezha.enums.TransportType;

public record CreateTransportAdminRequestDTO(
        Long ownerId,
        Boolean canBeRented,
        TransportType transportType,
        String model,
        String color,
        String identifier,
        String description,
        Double latitude,
        Double longitude,
        Double minutePrice,
        Double dayPrice
) { }
