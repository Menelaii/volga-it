package com.example.volgaitzhezha.models.dtos.accounts;

public record CreateAccountAdminRequestDTO(
        String username,
        String password,
        Boolean isAdmin,
        Double balance
) { }