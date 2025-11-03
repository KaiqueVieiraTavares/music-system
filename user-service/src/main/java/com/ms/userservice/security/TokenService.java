package com.ms.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {
    private final SecretKey key;

    public TokenService(@Value("${secret.jwt}")String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(Date.from(Instant.now())).expiration(Date.from(Instant.now().plus(3, ChronoUnit.HOURS)))
                .signWith(key).compact();
    }
    public String verifyAndExtractEmailToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
