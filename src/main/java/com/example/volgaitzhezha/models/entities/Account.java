package com.example.volgaitzhezha.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity {
    private String username;
    private String password;
    private String role;
    private boolean isLocked;

    @Transient
    private boolean isAdmin;
}
