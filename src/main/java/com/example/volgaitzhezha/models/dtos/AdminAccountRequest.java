package com.example.volgaitzhezha.models.dtos;

public record AdminAccountRequest(
        String username,
        String password,
        Boolean isAdmin,
        Double balance
) { }