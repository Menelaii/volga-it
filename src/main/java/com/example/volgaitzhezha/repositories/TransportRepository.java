package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
}
