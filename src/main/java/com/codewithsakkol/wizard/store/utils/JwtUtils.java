package com.codewithsakkol.wizard.store.utils;

import com.codewithsakkol.wizard.store.config.JwtConfig;
import com.codewithsakkol.wizard.store.entities.enums.Rols;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId, Map<String, Object> claims) {
        return buildToken(userId.toString(), claims, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Long userId, Map<String, Object> claims) {
        return buildToken(userId.toString(), claims, jwtConfig.getRefreshTokenExpiration());
    }

    private String buildToken(String subject, Map<String, Object> claims, long expirationMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * expirationMillis))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public Rols getRole(String token) {
        String roleStr = extractClaim(token, claims -> claims.get("role", String.class));
        return Rols.valueOf(roleStr);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}