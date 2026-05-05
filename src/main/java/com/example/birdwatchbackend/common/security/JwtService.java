package com.example.birdwatchbackend.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long expirationMs;

    public JwtService(JwtEncoder encoder, @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.encoder = encoder;
        this.expirationMs = expirationMs;
    }

    public String generateToken(UUID userId, String email, String role) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .claim("email", email)
                .claim("role", role)
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}