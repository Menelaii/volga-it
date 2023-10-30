package com.example.volgaitzhezha.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = @Index(columnList = "token"))
public class TokenGuard extends AbstractEntity {
    private Boolean isTokenEnabled;
    private String token;
}
