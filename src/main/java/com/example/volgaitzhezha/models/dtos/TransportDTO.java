package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.TransportModel;

public record TransportDTO(
        Boolean canBeRented,
        TransportModel transportType,
        String model,
        String color,
        String identifier,
        String description,
        Double latitude,
        Double longitude,
        Double minutePrice,
        Double dayPrice
) {}
