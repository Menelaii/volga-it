package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.repositories.AccountsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {
    private final AccountsService service;
    private final AccountsRepository repository;

    @Transactional
    public void deposit(Long accountId, Double amount) {
        Account current = service.getAuthenticated();
        if (!current.isAdmin() && (!Objects.equals(current.getId(), accountId))) {
            throw new IllegalStateException("Пользователь может пополнить баланс только самому себе");
        }

        if (!repository.existsById(accountId)) {
            throw new EntityNotFoundException();
        }

        repository.deposit(accountId, amount);
    }
}
