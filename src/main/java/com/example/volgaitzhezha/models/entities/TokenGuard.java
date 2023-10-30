package com.example.volgaitzhezha.models.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TokenGuard extends AbstractEntity {
    private Boolean isActive;
}
