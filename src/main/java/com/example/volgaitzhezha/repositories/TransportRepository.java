package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT * FROM transport WHERE transportType = :transportType" +
            "OFFSET :start LIMIT :count", nativeQuery = true)
    List<Transport> findAll(
            @Param("transportType") String transportType,
            @Param("start") Integer start,
            @Param("count") Integer count
    );

    //todo
    @Query(value = "SELECT *, calculate_distance(:latitude, :longitude, latitude, longitude) AS distance" +
            " FROM transport" +
            " WHERE canBeRented = true" +
            " AND (:type = 'ALL' OR transportType = :type)" +
            " AND distance <= :radius" +
            " ORDER BY distance",
            nativeQuery = true)
    List<Transport> getAvailable(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("type") String type
    );
}
