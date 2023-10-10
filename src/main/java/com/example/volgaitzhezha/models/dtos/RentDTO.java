package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.RentType;

import java.time.LocalDateTime;

public class RentDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RentType rentType;
    private TransportDTO transport;
    private AccountInfoDTO renter;
}
