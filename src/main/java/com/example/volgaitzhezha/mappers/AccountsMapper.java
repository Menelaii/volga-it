package com.example.volgaitzhezha.mappers;

import com.example.volgaitzhezha.models.dtos.AccountDTO;
import com.example.volgaitzhezha.models.dtos.AccountInfoDTO;
import com.example.volgaitzhezha.models.dtos.AdminAccountDTO;
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

    public AccountInfoDTO map(Account account) {
        AccountInfoDTO dto = modelMapper.map(account, AccountInfoDTO.class);
        dto.setIsAdmin(account.isAdmin());
        return dto;
    }

    public Account map(AccountDTO dto) {
        Account account = modelMapper.map(dto, Account.class);
        account.setRole(DEFAULT_ROLE);
        return account;
    }

    public Account map(AdminAccountDTO dto) {
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
