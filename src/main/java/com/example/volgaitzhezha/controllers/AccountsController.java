package com.example.volgaitzhezha.controllers;

import com.example.volgaitzhezha.models.dtos.AccountDTO;
import com.example.volgaitzhezha.models.dtos.AccountInfoDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.security.jwt.JwtUtil;
import com.example.volgaitzhezha.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.example.volgaitzhezha.utils.Constants.DEFAULT_ROLE;

@RestController
@RequestMapping("/api/Account")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService accountsService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.tokenExpiresIn}")
    private int tokenExpiresIn;

    @GetMapping("/Me")
    public ResponseEntity<AccountInfoDTO> getCurrentAccount() {
        Account me = accountsService.getAuthenticated();
        return ResponseEntity.ok(modelMapper.map(me, AccountInfoDTO.class));
    }

    @PostMapping("/SignIn")
    public ResponseEntity<String> signIn(@RequestBody AccountDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        String token = jwtUtil.generateToken(request.username(), tokenExpiresIn);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/SignUp")
    public ResponseEntity<String> signUp(@RequestBody AccountDTO request) {
        accountsService.register(
                modelMapper.map(request, Account.class),
                DEFAULT_ROLE
        );

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/SignOut")
    public ResponseEntity<String> signOut() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/Update")
    public ResponseEntity<String> updateAccount(@RequestBody AccountDTO request) {
        Account updated = modelMapper.map(request, Account.class);
        accountsService.updateOwnAccount(updated);
        return ResponseEntity.ok().build();
    }
}
