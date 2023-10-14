package com.example.volgaitzhezha.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.example.volgaitzhezha.utils.Constants.ADMIN_ROLE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity {
    private String username;
    private String password;
    private String role;
    private Double balance;
    private Boolean isLocked;

    @OneToMany(mappedBy = "renter")
    private List<Rent> rents;

    @OneToMany(mappedBy = "owner")
    private List<Transport> transport;

    public Account(String username, String password, String role, Double balance, Boolean isLocked) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = balance;
        this.isLocked = isLocked;
    }

    public boolean isAdmin() {
        return ADMIN_ROLE.equals(role);
    }
}
