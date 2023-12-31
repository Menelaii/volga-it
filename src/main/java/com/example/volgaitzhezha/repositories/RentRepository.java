package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findAllByTransportId(Long transportId);

    List<Rent> findAllByRenterId(Long renterId);
}
