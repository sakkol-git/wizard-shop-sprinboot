package com.codewithsakkol.wizard.store.auth;

import com.codewithsakkol.wizard.store.auth.TokenBlacklistRepository;
import com.codewithsakkol.wizard.store.auth.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import com.codewithsakkol.wizard.store.auth.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Skip if there is no Authorization header or it lacks the "Bearer " prefix
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the actual JWT token safely
        final String token = authHeader.substring(7);

        // 3. Skip if the request is already authenticated in the SecurityContext
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (tokenBlacklistRepository.existsByToken(token)) {
                log.debug("Token is blacklisted.");
                filterChain.doFilter(request, response);
                return;
            }

            // 4. Validate token and construct Authentication object
            if (jwtUtils.validateToken(token)) {
                var role = jwtUtils.getRole(token);
                var userId = jwtUtils.getUserId(token);
                var email = jwtUtils.extractClaim(token, claims -> claims.get("email", String.class));

                CustomUserDetails userDetails = new CustomUserDetails(userId, email, role.name());

                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, // Principal
                        null,   // Credentials
                        userDetails.getAuthorities()
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 5. Save the authentication to the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Successfully authenticated user ID: {}", userId);
            }
        } catch (Exception e) {
            // Log the error but don't break the application; anonymous access will handle the rejection
            log.error("Failed to set user authentication in security context: {}", e.getMessage());
        }

        // 6. Always pass the request down the chain
        filterChain.doFilter(request, response);
    }
}