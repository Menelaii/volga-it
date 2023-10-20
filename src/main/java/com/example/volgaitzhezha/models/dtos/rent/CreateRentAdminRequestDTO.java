package com.example.volgaitzhezha.models.dtos.rent;

import com.example.volgaitzhezha.enums.RentType;

import java.time.LocalDateTime;

public record CreateRentAdminRequestDTO(
        Long transportId,
        Long userId,
        LocalDateTime timeStart,
        LocalDateTime timeEnd,
        Double priceOfUnit,
        RentType priceType,
        Double finalPrice
) { }