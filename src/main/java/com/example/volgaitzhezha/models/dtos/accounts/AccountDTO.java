package com.example.volgaitzhezha.models.dtos.accounts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private Long id;
    private String username;
    private Boolean isAdmin;
    private Double balance;
}
