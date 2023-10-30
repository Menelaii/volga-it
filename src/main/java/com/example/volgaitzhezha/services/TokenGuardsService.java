package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.TokenGuard;
import com.example.volgaitzhezha.repositories.TokensRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenGuardsService {
    private final TokensRepository repository;

    @Transactional
    public void watchToken(String token) {
        TokenGuard tokenGuard = new TokenGuard();
        tokenGuard.setToken(token);
        tokenGuard.setIsTokenEnabled(true);
        repository.save(tokenGuard);
    }

    @Transactional
    public void disableToken(String token) {
        TokenGuard tokenGuard = repository.findByToken(token)
                .orElseThrow(EntityNotFoundException::new);

        repository.disableToken(tokenGuard.getId());
    }

    public boolean isTokenEnabled(String token) {
        TokenGuard guard = repository.findByToken(token).orElse(null);
        return guard != null && guard.getIsTokenEnabled() != null && guard.getIsTokenEnabled();
    }
}
