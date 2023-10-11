package com.example.volgaitzhezha.models.entities;

import com.example.volgaitzhezha.enums.RentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rent extends AbstractEntity {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double priceOfUnit;
    private Double finalPrice;

    @Enumerated(EnumType.STRING)
    private RentType rentType;

    @ManyToOne
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    private Transport transport;

    @ManyToOne
    @JoinColumn(name = "renter_id", referencedColumnName = "id")
    private Account renter;

    public void startRent() {
        this.startTime = LocalDateTime.now();
    }

    public boolean isRentEnded() {
        return this.endTime != null;
    }
}