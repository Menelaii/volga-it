package com.example.volgaitzhezha.models.dtos.rent;

import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserHistoryRentDTO extends RentDTO {
    private TransportDTO transport;
}
