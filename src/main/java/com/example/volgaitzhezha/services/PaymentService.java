package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.repositories.AccountsRepository;
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
        Account currentUser = service.getAuthenticated();
        if (!isAdminOrOwner(accountId, currentUser)) {
            throw new ApiRequestException("Пользователь может пополнить баланс только самому себе");
        }

        if (!repository.existsById(accountId)) {
            throw new ApiRequestException("Аккаунт не существует");
        }

        repository.deposit(accountId, amount);
    }

    @Transactional
    public void processPayment(Long payerId, Long payeeId, Double amount) {
        Account payer = service.getById(payerId);
        Account payee = service.getById(payeeId);
        processPayment(payer, payee, amount);
    }

    /**
     * Отрицательный баланс допускается
     */
    @Transactional
    public void processPayment(Account payer, Account payee, Double amount) {
        payer.setBalance(payer.getBalance() - amount);
        payee.setBalance(payee.getBalance() + amount);
    }

    private boolean isAdminOrOwner(Long accountId, Account current) {
        return current.isAdmin() || Objects.equals(current.getId(), accountId);
    }
}
