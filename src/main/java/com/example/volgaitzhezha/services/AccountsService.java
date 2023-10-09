package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.repositories.AccountsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountsService {
    private final static String DEFAULT_ROLE = "ROLE_USER";

    private final AccountsRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Account account) {
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        account.setRole(DEFAULT_ROLE);

        repository.save(account);
    }

    @Transactional
    public void updateOwnAccount(Account updated) {
        Account existingAccount = getCurrentAccount();

        String newUsername = updated.getUsername();
        if (!newUsername.equals(existingAccount.getUsername()) &&
                repository.findByUsername(newUsername).isPresent()) {
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        }

        existingAccount.setUsername(updated.getUsername());
        existingAccount.setPassword(updated.getPassword());

        repository.save(existingAccount);
    }

    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new IllegalStateException();
        }

        return repository.findByUsername(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Account> getAccounts(XPage page) {
        return repository.findAll(page.getStart(), page.getCount());
    }

    public Account getById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        repository.deleteById(id);
    }
}
