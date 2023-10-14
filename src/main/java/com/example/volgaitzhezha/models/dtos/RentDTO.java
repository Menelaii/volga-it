package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.RentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RentType rentType;
    private TransportDTO transport;
    private AccountInfoDTO renter;
}
