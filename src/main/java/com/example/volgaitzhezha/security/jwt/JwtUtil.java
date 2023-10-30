package com.example.volgaitzhezha.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.volgaitzhezha.services.TokenGuardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.subject}")
    private String subject;

    private final TokenGuardsService tokenGuardsService;

    public String generateToken(long id, String username, int expiresIn) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusSeconds(expiresIn).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username", username)
                .withClaim("id", id)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Optional<String> validateTokenAndRetrieveClaim(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject(subject)
                    .withIssuer(issuer)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            Long id = decodedJWT.getClaim("id").asLong();
            if (!tokenGuardsService.isTokenEnabled(id)) {
                throw new JWTVerificationException("Токен не действителен");
            }

            return Optional.of(decodedJWT.getClaim("username").asString());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }

    public Optional<Long> retrieveId(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject(subject)
                    .withIssuer(issuer)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return Optional.of(decodedJWT.getClaim("id").asLong());
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
