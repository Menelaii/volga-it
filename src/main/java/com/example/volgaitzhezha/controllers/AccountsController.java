package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.mappers.AccountsMapper;
import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.models.dtos.AccountDTO;
import com.example.volgaitzhezha.models.dtos.AccountInfoDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.security.jwt.JwtUtil;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Account")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService accountsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AccountsMapper mapper;

    @Value("${jwt.tokenExpiresIn}")
    private int tokenExpiresIn;

    @GetMapping("/Me")
    public ResponseEntity<AccountInfoDTO> getCurrentAccount() {
        Account me = accountsService.getAuthenticated();
        return ResponseEntity.ok(mapper.map(me));
    }

    @PostMapping("/SignIn")
    public ResponseEntity<String> signIn(@RequestBody AccountDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiRequestException("Неправильные логин или пароль");
        }

        String token = jwtUtil.generateToken(request.username(), tokenExpiresIn);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/SignUp")
    public ResponseEntity<Void> signUp(@RequestBody AccountDTO request) {
        accountsService.register(mapper.map(request));
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/SignOut")
    public ResponseEntity<Void> signOut() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new ApiRequestException("Пользователь не аутентифицирован");
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/Update")
    public ResponseEntity<Void> updateAccount(@RequestBody AccountDTO request) {
        Account updated = mapper.map(request);
        accountsService.updateOwnAccount(updated);
        return ResponseEntity.ok().build();
    }
}
