package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.repositories.TransportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransportService {
    private final AccountsService accountsService;
    private final TransportRepository repository;

    public Transport getById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Transport add(Transport transport, Long ownerId) {
        if (accountsService.getAuthenticated().isAdmin() && ownerId != null) {
            transport.setOwner(accountsService.getById(ownerId));
        }

        return repository.save(transport);
    }

    @Transactional
    public Transport update(Long id, Transport updatedEntity) {
        Transport existingEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        updatedEntity.setId(id);
        
        if (!Objects.equals(updatedEntity.getOwner(), existingEntity.getOwner())
                && !accountsService.getAuthenticated().isAdmin()) {
            throw new IllegalStateException("Попытка пользователя установить нового владельца");
        }

        return repository.save(existingEntity);
    }

    @Transactional
    public void delete(Long id) {
        Transport existingTransport = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Account authenticated = accountsService.getAuthenticated();
        if (!authenticated.isAdmin() && !isOwner(authenticated, existingTransport)) {
            throw new IllegalStateException("Вы не владеете этим транспортом");
        }

        repository.delete(existingTransport);
    }

    public List<Transport> getAll(XPage page, TransportType transportType) {
        return repository.findAll(transportType.name(), page.getStart(), page.getCount());
    }

    public List<Transport> getAllAvailable(Double latitude, Double longitude,
                                           Double radius, TransportType type
    ) {
        return repository.getAvailable(
                latitude, longitude,
                radius, type.name()
        );
    }

    private boolean isOwner(Account account, Transport transport) {
        return Objects.equals(account.getId(), transport.getOwner().getId());
    }
}
