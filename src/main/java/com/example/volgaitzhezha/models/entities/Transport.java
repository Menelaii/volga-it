package com.example.volgaitzhezha.models.entities;

import com.example.volgaitzhezha.enums.TransportType;
import jakarta.persistence.*;
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
    private String model;
    private String color;
    private String identifier;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double minutePrice;
    private Double dayPrice;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Account owner;
}
