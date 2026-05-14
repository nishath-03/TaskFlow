package com.taskflow.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Injected from application.properties jwt.secret
    @Value("${jwt.secret}")
    private String secret;

    // Injected from application.properties jwt.expiration (86400000 = 24 hours in ms)
    @Value("${jwt.expiration}")
    private long expiration;

    // Build a signing key from our secret string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Called after successful login — creates a JWT containing email + role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)              // who owns this token
                .claim("role", role)            // custom claim: user's role
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from token — used by JwtFilter to identify the user
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // Extract role from token — used to set Spring Security authorities
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // Validate: signature correct + not expired
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
