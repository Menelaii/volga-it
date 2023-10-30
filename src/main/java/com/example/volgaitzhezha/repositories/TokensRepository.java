package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.TokenGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends JpaRepository<TokenGuard, Long> {
    @Modifying
    @Query(value = "UPDATE TokenGuard t SET t.isActive = false")
    void setAsInvalid(Long id);
}
