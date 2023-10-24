package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.repositories.AccountsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public void deposit(Long accountId, Double amount) {
        Account currentUser = service.getAuthenticated();
        if (!isAdminOrOwner(accountId, currentUser)) {
            throw new ApiRequestException("Пользователь может пополнить баланс только самому себе");
        }

        if (!repository.existsById(accountId)) {
            throw new EntityNotFoundException();
        }

        repository.deposit(accountId, amount);
    }

    /**
     * Отрицательный баланс допускается.
     */
    @Transactional
    public void processPayment(Account payer, Account payee, Double amount) {
        if (!entityManager.contains(payer)) {
            payer = service.getById(payer.getId());
        }

        if (!entityManager.contains(payee)) {
            payee = service.getById(payee.getId());
        }

        payer.setBalance(payer.getBalance() - amount);
        payee.setBalance(payee.getBalance() + amount);
    }

    private boolean isAdminOrOwner(Long accountId, Account current) {
        return current.isAdmin() || Objects.equals(current.getId(), accountId);
    }
}
