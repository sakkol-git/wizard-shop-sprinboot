package com.codewithsakkol.wizard.store.auth.jwt;

import com.codewithsakkol.wizard.store.auth.CustomUserDetails;
import com.codewithsakkol.wizard.store.auth.LoginRequest;
import com.codewithsakkol.wizard.store.auth.TokenBlacklist;
import com.codewithsakkol.wizard.store.auth.TokenBlacklistRepository;
import com.codewithsakkol.wizard.store.users.User;
import com.codewithsakkol.wizard.store.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthJwtService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtUtils jwtUtils;

    // Helper object to pass both tokens back to the controller cleanly
    public record AuthTokens(String accessToken, String refreshToken) {}

    public AuthTokens login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String roleStr = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        return generateTokensForUser(userDetails.getId(), userDetails.getEmail(), userDetails.getName(), roleStr);
    }

    public void logout(String token) {
        if (jwtUtils.validateToken(token) && !tokenBlacklistRepository.existsByToken(token)) {
            java.util.Date expiration = jwtUtils.extractClaim(token, io.jsonwebtoken.Claims::getExpiration);
            tokenBlacklistRepository.save(TokenBlacklist.builder()
                    .token(token)
                    .expiresAt(expiration)
                    .build());
        }
    }

    public AuthTokens refreshTokens(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Long userId = jwtUtils.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for provided token"));

        return generateTokensForUser(user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationServiceException("Authentication object is null");
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthenticationServiceException("Invalid authentication principal");
        }

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new AuthenticationServiceException("User not found"));
    }

    private AuthTokens generateTokensForUser(Long id, String email, String name, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("email", email);
        claims.put("role", role);

        String accessToken = jwtUtils.generateAccessToken(id, claims);
        String refreshToken = jwtUtils.generateRefreshToken(id, claims);

        return new AuthTokens(accessToken, refreshToken);
    }


}