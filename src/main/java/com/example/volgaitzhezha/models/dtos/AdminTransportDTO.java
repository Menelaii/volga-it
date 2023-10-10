package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.TransportType;

public record AdminTransportDTO(
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
