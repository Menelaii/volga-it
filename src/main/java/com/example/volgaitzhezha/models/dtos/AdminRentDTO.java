package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.RentType;

import java.time.LocalDateTime;

public record AdminRentDTO(
        Long transportId,
        Long userId,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Double priceOfUnit,
        RentType priceType,
        Double finalPrice
) { }