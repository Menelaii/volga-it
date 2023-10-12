package com.example.volgaitzhezha.models.dtos;

import com.example.volgaitzhezha.enums.TransportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportDTO {
    private Boolean canBeRented;
    private TransportType transportType;
    private String model;
    private String color;
    private String identifier;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double minutePrice;
    private Double dayPrice;
}
