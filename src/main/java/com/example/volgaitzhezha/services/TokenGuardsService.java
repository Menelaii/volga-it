package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.TokenGuard;
import com.example.volgaitzhezha.repositories.TokensRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenGuardsService {
    private final TokensRepository repository;

    @Transactional
    public TokenGuard watchToken() {
        TokenGuard tokenGuard = new TokenGuard();
        tokenGuard.setIsActive(true);
        return repository.save(tokenGuard);
    }

    @Transactional
    public void setAsInvalidToken(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        repository.setAsInvalid(id);
    }

    public boolean isTokenEnabled(Long id) {
        Optional<TokenGuard> token = repository.findById(id);
        return token.isPresent() && token.get().getIsActive() != null && token.get().getIsActive();
    }
}
