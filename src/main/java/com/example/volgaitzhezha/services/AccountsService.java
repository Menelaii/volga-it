package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.repositories.AccountsRepository;
import com.example.volgaitzhezha.security.userDetails.UserDetailsImpl;
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
    private final AccountsRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Account account, String role) {
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        account.setRole(role);

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
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        }

        updatedAccount.setId(existingAccount.getId());

        repository.save(existingAccount);
    }

    public Account getAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new IllegalStateException();
        }

        return ((UserDetailsImpl) authentication.getPrincipal()).getAccount();
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
