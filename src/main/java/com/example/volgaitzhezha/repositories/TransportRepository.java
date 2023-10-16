package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT * FROM transport WHERE transport_type = :transportType" +
            " OR :transportType = 'ALL'" +
            " OFFSET :start LIMIT :count", nativeQuery = true)
    List<Transport> findAll(
            @Param("transportType") String transportType,
            @Param("start") Integer start,
            @Param("count") Integer count
    );

    @Query(value =
            "SELECT * " +
                    "FROM Transport t " +
                    "WHERE t.can_be_rented = true " +
                    "AND (:type = 'ALL' OR t.transport_type = :type) " +
                    "AND calculate_distance(:latitude, :longitude, t.latitude, t.longitude) <= :radius " +
                    "ORDER BY calculate_distance(:latitude, :longitude, t.latitude, t.longitude)",
            nativeQuery = true)
    List<Transport> getAvailable(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius,
            @Param("type") String type
    );

    @Modifying
    @Query("UPDATE Transport t " +
            "SET t.canBeRented = true, t.latitude = :latitude, t.longitude = :longitude " +
            "WHERE t.id = :id")
    void endRent(
            @Param("id") Long id,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude
    );
}
