package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.TokenGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<TokenGuard, Long> {
    @Modifying
    @Query(value = "UPDATE TokenGuard t" +
            " SET t.isTokenEnabled = false" +
            " WHERE t.id = :id")
    void disableToken(@Param("id") Long id);

    Optional<TokenGuard> findByToken(String token);
}
