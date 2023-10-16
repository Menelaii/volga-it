package com.example.volgaitzhezha.controllers.admin;

import com.example.volgaitzhezha.mappers.AccountsMapper;
import com.example.volgaitzhezha.models.dtos.AccountInfoDTO;
import com.example.volgaitzhezha.models.dtos.AdminAccountDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/Account")
@RequiredArgsConstructor
public class AdminAccountsController {
    private final AccountsService service;
    private final AccountsMapper mapper;

    @GetMapping
    public ResponseEntity<List<AccountInfoDTO>> getAllAccounts(XPage page) {
        List<AccountInfoDTO> body = service.getAccounts(page)
                .stream()
                .map(mapper::map)
                .toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountInfoDTO> getAccountById(@PathVariable("id") Long id) {
        Account account = service.getById(id);
        return ResponseEntity.ok(mapper.map(account));
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AdminAccountDTO request) {
        service.register(mapper.map(request));
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable("id") Long id,
                                                @RequestBody AdminAccountDTO request
    ) {
        Account updatedAccount = mapper.map(request);
        service.update(id, updatedAccount);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
