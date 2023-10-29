package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.annotations.AdminOnly;
import com.example.volgaitzhezha.enums.RentType;
import com.example.volgaitzhezha.exceptions.ApiRequestException;
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
    private final PaymentService paymentService;

    @Transactional
    public void save(Rent rent, Long transportId, Long renterId) {
        if (!accountsService.getAuthenticated().isAdmin()) {
            throw new ApiRequestException("Недостаточно прав");
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
            throw new ApiRequestException("Нельзя арендовать свой транспорт");
        }

        if (!transport.getCanBeRented()) {
            throw new ApiRequestException("Этот транспорт сейчас недоступен");
        }

        if (account.getBalance() <= 0) {
            throw new ApiRequestException("Недостаточно средств");
        }

        transport.setCanBeRented(false);

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
                .orElseThrow(() -> new ApiRequestException("Запись не найдена"));

        Account account = accountsService.getAuthenticated();
        if (!account.isAdmin() && !isRenter(account, rent)) {
            throw new ApiRequestException("Завершить может только человек который создавал эту аренду");
        }

        transportService.endRent(rent.getTransport(), latitude, longitude);

        rent.setEndTime(LocalDateTime.now());

        long units = rent.getRentType() == RentType.DAYS
                ? daysBetween(rent.getStartTime(), rent.getEndTime())
                : minutesBetween(rent.getStartTime(), rent.getEndTime());

        Double finalPrice = units * rent.getPriceOfUnit();
        rent.setFinalPrice(finalPrice);
        paymentService.processPayment(rent.getRenter(), rent.getTransport().getOwner(), finalPrice);

        repository.save(rent);
    }

    public List<Rent> getTransportHistory(Long transportId) {
        Transport transport = transportService.getById(transportId);
        if (!isOwnerOrAdmin(accountsService.getAuthenticated(), transport)) {
            throw new ApiRequestException("Доступно только владельцу транспорта");
        }

        return repository.findAllByTransportId(transportId);
    }

    public List<Rent> getMyHistory() {
        return repository.findAllByRenterId(accountsService.getAuthenticated().getId());
    }

    @AdminOnly
    public List<Rent> getUserHistory(Long userId) {
        return repository.findAllByRenterId(userId);
    }

    public Rent getById(Long id) {
        Rent rent = repository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Запись не найдена"));

        Account account = accountsService.getAuthenticated();
        if (!account.isAdmin() && !isRenterOrOwner(account, rent)) {
            throw new ApiRequestException("Доступно только арендатору или владелецу транспорта");
        }

        return rent;
    }

    @AdminOnly
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        repository.deleteById(id);
    }

    @AdminOnly
    @Transactional
    public void update(Long id, Rent updatedEntity, Long transportId, Long userId) {
        Rent existingEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Account account = accountsService.getById(userId);
        Transport transport = transportService.getById(transportId);

        updatedEntity.setId(id);
        updatedEntity.setRenter(account);
        updatedEntity.setTransport(transport);

        if (Objects.equals(!existingEntity.isRentEnded(), updatedEntity.isRentEnded())) {
            if (Objects.isNull(updatedEntity.getFinalPrice())) {
                throw new ApiRequestException("Необходимо указать итоговую цену");
            }

            paymentService.processPayment(
                    updatedEntity.getRenter(),
                    updatedEntity.getTransport().getOwner(),
                    updatedEntity.getFinalPrice()
            );
        }

        repository.save(updatedEntity);
    }

    private boolean isRenterOrOwner(Account account, Rent rent) {
        return isRenter(account, rent) || isOwner(account, rent.getTransport());
    }

    private boolean isOwnerOrAdmin(Account account, Transport transport) {
        return account.isAdmin() || isOwner(account, transport);
    }

    private boolean isRenter(Account account, Rent rent) {
        return Objects.equals(account, rent.getRenter());
    }

    private boolean isOwner(Account account, Transport transport) {
        return Objects.equals(transport.getOwner(), account);
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
