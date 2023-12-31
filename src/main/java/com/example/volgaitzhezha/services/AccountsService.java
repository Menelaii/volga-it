package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.annotations.AdminOnly;
import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.repositories.AccountsRepository;
import com.example.volgaitzhezha.security.userDetails.UserDetailsImpl;
import com.example.volgaitzhezha.security.userDetails.UserDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountsService {
    private final AccountsRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    public void register(Account account) {
        if (repository.findByUsername(account.getUsername()).isPresent()) {
            throw new ApiRequestException("Пользователь с таким именем уже существует");
        }

        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        if (account.getBalance() == null) {
            account.setBalance(0d);
        }

        repository.save(account);
    }

    @Transactional
    public void updateOwnAccount(Account updatedAccount) {
        Account existingAccount = getAuthenticated();
        update(existingAccount, updatedAccount);
    }

    @Transactional
    public void update(Long id, Account updatedAccount) {
        Account existingAccount = getById(id);
        update(existingAccount, updatedAccount);
    }

    @Transactional
    public void update(Account existingAccount, Account updatedAccount) {
        String newUsername = updatedAccount.getUsername();
        if (!newUsername.equals(existingAccount.getUsername()) &&
                repository.findByUsername(newUsername).isPresent()) {
            throw new ApiRequestException("Пользователь с таким именем уже существует");
        }

        if (!Objects.equals(existingAccount.getRole(), updatedAccount.getRole())
                && !getAuthenticated().isAdmin()) {
            throw new ApiRequestException("Нет прав для изменения роли");
        }

        updatedAccount.setId(existingAccount.getId());
        updatedAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));

        repository.save(updatedAccount);
    }

    public Account getAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new ApiRequestException("Пользователь не аутентифицирован");
        }

        return ((UserDetailsImpl) authentication.getPrincipal()).getAccount();
    }

    @AdminOnly
    public List<Account> getAccounts(XPage page) {
        return repository.findAll(page.getStart(), page.getCount());
    }

    public Account getById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @AdminOnly
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        repository.deleteById(id);
    }
}
