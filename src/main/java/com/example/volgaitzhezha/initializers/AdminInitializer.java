package com.example.volgaitzhezha.initializers;

import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.security.userDetails.UserDetailsServiceImpl;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static com.example.volgaitzhezha.utils.Constants.ADMIN_ROLE;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final AccountsService service;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.balance}")
    private Double balance;

    @Override
    public void run(String... args) throws Exception {
        try {
            UserDetails admin = userDetailsService.loadUserByUsername(username);
            logger.info("Admin " + admin.getUsername() + " already created, skipping");
        } catch (UsernameNotFoundException e) {
            Account admin = new Account(
                    username,
                    password,
                    balance,
                    false
            );

            service.register(
                    admin,
                    ADMIN_ROLE
            );

            logger.info("Admin created successfully");
        }
    }
}
