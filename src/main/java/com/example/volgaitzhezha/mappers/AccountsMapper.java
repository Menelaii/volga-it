package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.accounts.AuthRequestDTO;
import com.example.volgaitzhezha.models.dtos.accounts.AccountDTO;
import com.example.volgaitzhezha.models.dtos.accounts.CreateAccountAdminRequestDTO;
import com.example.volgaitzhezha.models.entities.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static com.example.volgaitzhezha.utils.Constants.ADMIN_ROLE;
import static com.example.volgaitzhezha.utils.Constants.DEFAULT_ROLE;

@Component
@RequiredArgsConstructor
public class AccountsMapper {
    private final ModelMapper modelMapper;

    public AccountDTO map(Account account) {
        AccountDTO dto = modelMapper.map(account, AccountDTO.class);
        dto.setIsAdmin(account.isAdmin());
        return dto;
    }

    public Account map(AuthRequestDTO dto) {
        Account account = modelMapper.map(dto, Account.class);
        account.setRole(DEFAULT_ROLE);
        return account;
    }

    public Account map(CreateAccountAdminRequestDTO dto) {
        Account account = modelMapper.map(dto, Account.class);
        account.setRole(dto.isAdmin() ? ADMIN_ROLE : DEFAULT_ROLE);
        return account;
    }

    public Account map(Long id) {
        Account account = new Account();
        account.setId(id);
        return account;
    }
}
