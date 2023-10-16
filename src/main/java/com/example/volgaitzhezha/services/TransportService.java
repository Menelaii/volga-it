package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.models.pagination.XPage;
import com.example.volgaitzhezha.repositories.TransportRepository;
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
                .orElseThrow(() -> new ApiRequestException("Транспорт не найден"));
    }

    @Transactional
    public void add(Transport transport) {
        Account currentAccount = accountsService.getAuthenticated();
        if (transport.getOwner() == null) {
            transport.setOwner(currentAccount);
        } else if (currentAccount.isAdmin()) {
            transport.setOwner(accountsService.getById(transport.getOwner().getId()));
        }

        repository.save(transport);
    }

    @Transactional
    public void update(Long id, Transport updatedEntity) {
        Transport existingEntity = repository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Транспорт не найден"));

        Account currentAccount = accountsService.getAuthenticated();
        if (!currentAccount.isAdmin()) {

            if (updatedEntity.getOwner() == null) {
                updatedEntity.setOwner(existingEntity.getOwner());
            }

            if (!Objects.equals(existingEntity.getOwner(), currentAccount)) {
                throw new ApiRequestException("Недостаточно прав");
            }

            if (!Objects.equals(updatedEntity.getOwner(), existingEntity.getOwner())) {
                throw new ApiRequestException("Недостаточно прав чтобы установить нового владельца");
            }
        }


        updatedEntity.setId(id);

        repository.save(updatedEntity);
    }

    @Transactional
    public void delete(Long id) {
        Transport existingTransport = repository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Транспорт не найден"));

        Account authenticated = accountsService.getAuthenticated();
        if (!authenticated.isAdmin() && !isOwner(authenticated, existingTransport)) {
            throw new ApiRequestException("Вы не владеете этим транспортом");
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
        return Objects.equals(account, transport.getOwner());
    }

    @Transactional
    public void endRent(Transport transport, Double latitude, Double longitude) {
        repository.endRent(transport.getId(), latitude, longitude);
        repository.save(transport);
    }
}
