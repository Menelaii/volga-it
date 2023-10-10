package com.example.volgaitzhezha.models.dtos;

public record AdminAccountDTO(
        String username,
        String password,
        Boolean isAdmin,
        Double balance
) { }