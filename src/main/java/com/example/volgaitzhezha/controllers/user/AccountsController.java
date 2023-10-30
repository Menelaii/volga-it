package com.example.volgaitzhezha.controllers.user;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.example.volgaitzhezha.mappers.AccountsMapper;
import com.example.volgaitzhezha.models.dtos.accounts.AccountDTO;
import com.example.volgaitzhezha.models.dtos.accounts.AuthRequestDTO;
import com.example.volgaitzhezha.models.entities.Account;
import com.example.volgaitzhezha.security.jwt.JwtUtil;
import com.example.volgaitzhezha.services.AccountsService;
import com.example.volgaitzhezha.services.TokenGuardsService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenGuardsService tokenGuardsService;

    @Value("${jwt.tokenExpiresIn}")
    private int tokenExpiresIn;

    @GetMapping("/Me")
    public ResponseEntity<AccountDTO> getCurrentAccount() {
        Account me = accountsService.getAuthenticated();
        return ResponseEntity.ok(mapper.map(me));
    }

    @PostMapping("/SignIn")
    public ResponseEntity<String> signIn(@RequestBody AuthRequestDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiRequestException("Неправильные логин или пароль");
        }

        String token = jwtUtil.generateToken(request.username(), tokenExpiresIn);
        tokenGuardsService.watchToken(token);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/SignUp")
    public ResponseEntity<Void> signUp(@RequestBody AuthRequestDTO request) {
        accountsService.register(mapper.map(request));
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/SignOut")
    public ResponseEntity<Void> signOut(HttpServletRequest request) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new ApiRequestException("Пользователь не аутентифицирован");
        }

        String token = request.getHeader("Authorization").substring(7);
        tokenGuardsService.disableToken(token);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/Update")
    public ResponseEntity<Void> updateAccount(@RequestBody AuthRequestDTO request) {
        Account updated = mapper.map(request);
        accountsService.updateOwnAccount(updated);
        return ResponseEntity.ok().build();
    }
}
