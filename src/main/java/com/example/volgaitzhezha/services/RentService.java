package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.enums.RentType;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.models.entities.Rent;
import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.repositories.RentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentService {
    private final RentRepository repository;
    private final AccountsService accountsService;
    private final TransportService transportService;

    @Transactional
    public void startRent(Long transportId, RentType rentType) {
        Account account = accountsService.getAuthenticated();
        Transport transport = transportService.getById(transportId);

        if (Objects.equals(account.getId(), transport.getOwner().getId())) {
            throw new IllegalStateException("Нельзя арендовать свой транспорт");
        }

        Rent rent = new Rent(
                LocalDateTime.now(),
                null,
                rentType,
                transport,
                account
        );

        repository.save(rent);
    }

    @Transactional
    public void endRent(Long id, Double latitude, Double longitude) {
        Rent rent = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (!Objects.equals(accountsService.getAuthenticated().getId(), rent.getRenter().getId())) {
            throw new IllegalStateException("Завершить может только человек который создавал эту аренду");
        }

        rent.endRent();
        transportService.endRent(rent.getTransport(), latitude, longitude);

        repository.save(rent);
    }

    public List<Rent> getHistory(Long transportId) {
        return repository.findAllByTransportId(transportId);
    }

    public List<Rent> getMyHistory() {
        return repository.findAllByRenterId(accountsService.getAuthenticated().getId());
    }

    public Rent getById(Long id) {
        Rent rent = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Account account = accountsService.getAuthenticated();
        if (!isRenterOrOwner(account, rent)) {
            throw new IllegalStateException("Доступно только арендатору или владелецу транспорта");
        }

        return rent;
    }

    private boolean isRenterOrOwner(Account account, Rent rent) {
        return Objects.equals(rent.getRenter().getId(), account.getId())
                || Objects.equals(rent.getTransport().getOwner().getId(), account.getId());
    }
}
