package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.AccountInfoDTO;
import com.example.volgaitzhezha.models.dtos.AdminAccountDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.volgaitzhezha.utils.Constants.ADMIN_ROLE;
import static com.example.volgaitzhezha.utils.Constants.DEFAULT_ROLE;

@RestController
@RequestMapping("/api/Admin/Account")
@RequiredArgsConstructor
public class AdminAccountsController {
    private final AccountsService service;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<AccountInfoDTO>> getAllAccounts(XPage page) {
        List<AccountInfoDTO> body = service.getAccounts(page)
                .stream()
                .map(account -> modelMapper.map(account, AccountInfoDTO.class))
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountInfoDTO> getAccountById(@PathVariable("id") Long id) {
        Account account = service.getById(id);
        return ResponseEntity.ok(modelMapper.map(account, AccountInfoDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AdminAccountDTO accountRequest) {
        service.register(
                modelMapper.map(accountRequest, Account.class),
                accountRequest.isAdmin() ? ADMIN_ROLE : DEFAULT_ROLE
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable("id") Long id,
                                                @RequestBody AdminAccountDTO accountRequest
    ) {
        Account updatedAccount = modelMapper.map(accountRequest, Account.class);
        updatedAccount.setRole(accountRequest.isAdmin() ? ADMIN_ROLE : DEFAULT_ROLE);
        service.update(id, updatedAccount);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
