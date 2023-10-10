package com.example.volgaitzhezha.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfoRequest {
    private Long id;
    private String username;
    private Boolean isAdmin;
    private Double balance;
}
