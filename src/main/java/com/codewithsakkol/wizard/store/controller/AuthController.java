package com.codewithsakkol.wizard.store.controller;

import com.codewithsakkol.wizard.store.config.JwtConfig;
import com.codewithsakkol.wizard.store.dtos.user.JwtRespond;
import com.codewithsakkol.wizard.store.dtos.user.LoginRequest;
import com.codewithsakkol.wizard.store.dtos.user.UserRespond;
import com.codewithsakkol.wizard.store.entities.User;
import com.codewithsakkol.wizard.store.mapper.UserMapper;
import com.codewithsakkol.wizard.store.repositories.UserRepository;
import com.codewithsakkol.wizard.store.service.AuthService;
import com.codewithsakkol.wizard.store.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository; // Optional: Can move 'me()' logic to a UserService later
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtRespond> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        log.info("Login attempt for email: {}", request.getEmail());

        AuthService.AuthTokens tokens = authService.login(request);
        setRefreshTokenCookie(response, tokens.refreshToken(), (int) (jwtConfig.getRefreshTokenExpiration() / 1000));

        return ResponseEntity.ok(new JwtRespond(tokens.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader, HttpServletResponse response) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        
        // Clear refresh token cookie
        setRefreshTokenCookie(response, "", 0);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserRespond> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(false);
        }
        String token = authHeader.substring(7);
        boolean isValid = jwtUtils.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtRespond> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            AuthService.AuthTokens tokens = authService.refreshTokens(refreshToken);
            // Optional: you can cycle the refresh token here by setting a new cookie
            return ResponseEntity.ok(new JwtRespond(tokens.accessToken()));
        } catch (RuntimeException e) {
            log.error("Refresh token failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, int maxAge) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}