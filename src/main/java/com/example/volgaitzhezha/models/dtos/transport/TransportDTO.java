package com.example.volgaitzhezha.models.dtos.transport;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.models.dtos.accounts.AccountDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportDTO {
    private Long id;
    private Boolean canBeRented;
    private String model;
    private String color;
    private String identifier;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double minutePrice;
    private Double dayPrice;
    private TransportType transportType;
    private AccountDTO owner;
}
