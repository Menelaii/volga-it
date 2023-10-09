package com.example.volgaitzhezha.models.entities;

import com.example.volgaitzhezha.enums.TransportModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transport extends AbstractEntity {

    private Boolean canBeRented;

    @Enumerated(EnumType.STRING)
    private TransportModel transportType;

    private String model;
    private String color;
    private String identifier;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double minutePrice;
    private Double dayPrice;
}
