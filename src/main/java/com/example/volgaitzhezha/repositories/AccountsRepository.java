package com.example.volgaitzhezha.repositories;

import com.example.volgaitzhezha.models.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    @Query(value = "SELECT * FROM account OFFSET :start LIMIT :count", nativeQuery = true)
    List<Account> findAll(@Param("start") Integer start,
                          @Param("count") Integer count);

    @Query(value = "UPDATE account SET balance = balance + :amount WHERE id = :accountId ", nativeQuery = true)
    void deposit(@Param("accountId") Long accountId,
                 @Param("amount") Double amount);
}
