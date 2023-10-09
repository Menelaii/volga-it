package com.example.volgaitzhezha.services;

import com.example.volgaitzhezha.models.entities.Transport;
import com.example.volgaitzhezha.repositories.TransportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransportService {
    private final TransportRepository repository;

    public List<Transport> getAllTransports() {
        return repository.findAll();
    }

    public Transport getById(Long id) {
        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Transport add(Transport transport) {
        return repository.save(transport);
    }

    @Transactional
    public Transport update(Long id, Transport updatedEntity) {
        Transport existingEntity = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        existingEntity.setCanBeRented(updatedEntity.getCanBeRented());
        existingEntity.setModel(updatedEntity.getModel());
        existingEntity.setColor(updatedEntity.getColor());
        existingEntity.setIdentifier(updatedEntity.getIdentifier());
        existingEntity.setDescription(updatedEntity.getDescription());
        existingEntity.setLatitude(updatedEntity.getLatitude());
        existingEntity.setLongitude(updatedEntity.getLongitude());
        existingEntity.setMinutePrice(updatedEntity.getMinutePrice());
        existingEntity.setDayPrice(updatedEntity.getDayPrice());

        return repository.save(existingEntity);
    }

    @Transactional
    public void delete(Long id) {
        Transport existingTransport = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.delete(existingTransport);
    }
}
