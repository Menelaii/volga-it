package com.example.volgaitzhezha.models.dtos.rent;

import com.example.volgaitzhezha.models.dtos.accounts.AccountDTO;
import com.example.volgaitzhezha.models.dtos.transport.TransportDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentFullDTO extends RentDTO {
    private TransportDTO transport;
    private AccountDTO renter;
}
