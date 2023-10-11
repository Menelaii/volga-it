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
import java.time.temporal.ChronoUnit;
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
    public void save(Rent rent, Long transportId, Long renterId) {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new IllegalStateException();
        }

        rent.setRenter(accountsService.getById(renterId));
        rent.setTransport(transportService.getById(transportId));

        repository.save(rent);
    }

    @Transactional
    public void startRent(Long transportId, RentType rentType) {
        Account account = accountsService.getAuthenticated();
        Transport transport = transportService.getById(transportId);

        if (isOwner(account, transport)) {
            throw new IllegalStateException("Нельзя арендовать свой транспорт");
        }

        Rent rent = new Rent(
                LocalDateTime.now(),
                null,
                getPriceOfUnit(transport, rentType),
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

        Account account = accountsService.getAuthenticated();
        if (!account.isAdmin() && !isRenter(account, rent)) {
            throw new IllegalStateException("Завершить может только человек который создавал эту аренду");
        }

        transportService.endRent(rent.getTransport(), latitude, longitude);

        rent.setEndTime(LocalDateTime.now());

        long units = rent.getRentType() == RentType.DAYS
                ? daysBetween(rent.getStartTime(), rent.getEndTime())
                : minutesBetween(rent.getStartTime(), rent.getEndTime());

        rent.setFinalPrice(units * rent.getPriceOfUnit());

        repository.save(rent);
    }

    public List<Rent> getTransportHistory(Long transportId) {
        Transport transport = transportService.getById(transportId);
        if (!isOwnerOrAdmin(accountsService.getAuthenticated(), transport)) {
            throw new IllegalStateException();
        }

        return repository.findAllByTransportId(transportId);
    }

    public List<Rent> getMyHistory() {
        return repository.findAllByRenterId(accountsService.getAuthenticated().getId());
    }

    public List<Rent> getUserHistory(Long userId) {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new IllegalStateException();
        }

        return repository.findAllByRenterId(userId);
    }

    public Rent getById(Long id) {
        Rent rent = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Account account = accountsService.getAuthenticated();
        if (!account.isAdmin() && !isRenterOrOwner(account, rent)) {
            throw new IllegalStateException("Доступно только арендатору или владелецу транспорта");
        }

        return rent;
    }

    @Transactional
    public void delete(Long id) {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new IllegalStateException();
        }

        if (!repository.existsById(id)) {
            throw new IllegalStateException();
        }

        repository.deleteById(id);
    }

    @Transactional
    public void update(Long id, Rent rent, Long transportId, Long userId) {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new IllegalStateException();
        }

        if (!repository.existsById(id)) {
            throw new IllegalStateException();
        }

        Account account = accountsService.getById(userId);
        Transport transport = transportService.getById(transportId);

        rent.setId(id);
        rent.setRenter(account);
        rent.setTransport(transport);

        repository.save(rent);
    }

    private boolean isRenterOrOwner(Account account, Rent rent) {
        return isRenter(account, rent) || isOwner(account, rent.getTransport());
    }

    private boolean isOwnerOrAdmin(Account account, Transport transport) {
        return account.isAdmin() || isOwner(account, transport);
    }

    private boolean isRenter(Account account, Rent rent) {
        return Objects.equals(account.getId(), rent.getRenter().getId());
    }

    private boolean isOwner(Account account, Transport transport) {
        return Objects.equals(transport.getOwner().getId(), account.getId());
    }

    private Double getPriceOfUnit(Transport transport, RentType rentType) {
        return rentType == RentType.DAYS
                ? transport.getDayPrice()
                : transport.getMinutePrice();
    }

    private long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    private long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }
}
