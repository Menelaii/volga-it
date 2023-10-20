package com.example.volgaitzhezha.models.dtos.rent;

import com.example.volgaitzhezha.models.dtos.accounts.AccountDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransportHistoryRentDTO extends RentDTO {
    private AccountDTO renter;
}
