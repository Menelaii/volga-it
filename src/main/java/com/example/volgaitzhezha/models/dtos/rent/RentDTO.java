package com.example.volgaitzhezha.models.dtos.rent;

import com.example.volgaitzhezha.enums.RentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RentType rentType;
}
