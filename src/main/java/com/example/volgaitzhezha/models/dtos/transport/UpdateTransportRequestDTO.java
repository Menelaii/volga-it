package com.example.volgaitzhezha.models.dtos.transport;

public record UpdateTransportRequestDTO(
        Boolean canBeRented,
        String model,
        String color,
        String identifier,
        String description,
        Double latitude,
        Double longitude,
        Double minutePrice,
        Double dayPrice
) { }
