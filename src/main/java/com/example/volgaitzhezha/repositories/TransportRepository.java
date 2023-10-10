package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT * FROM transport WHERE transportType = :transportType" +
            "OFFSET :start LIMIT :count", nativeQuery = true)
    List<Transport> findAll(String transportType, Integer start, Integer count);
}
