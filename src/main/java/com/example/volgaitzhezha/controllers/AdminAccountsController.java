package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.AccountDTO;
import com.example.volgaitzhezha.models.dtos.AccountViewDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/Account")
@RequiredArgsConstructor
public class AdminAccountsController {
    private final AccountsService accountsService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<AccountViewDTO>> getAllAccounts(XPage page) {
        List<AccountViewDTO> body = accountsService.getAccounts(page)
                .stream()
                .map(account -> modelMapper.map(account, AccountViewDTO.class))
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountViewDTO> getAccountById(@PathVariable("id") Long id) {
        Account account = accountsService.getById(id);
        return ResponseEntity.ok(modelMapper.map(account, AccountViewDTO.class));
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO) {
        // Логика создания нового аккаунта администратором
        return ResponseEntity.ok("Аккаунт успешно создан");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable("id") Long id,
                                                @RequestBody AccountDTO accountDTO) {
        // Логика изменения аккаунта администратором по id
        return ResponseEntity.ok("Аккаунт с id " + id + " успешно изменен");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long id) {
        accountsService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
