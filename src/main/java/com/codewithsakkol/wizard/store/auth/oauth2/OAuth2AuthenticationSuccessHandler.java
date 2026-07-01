package com.codewithsakkol.wizard.store.auth.oauth2;

import com.codewithsakkol.wizard.store.auth.CustomUserDetails;
import com.codewithsakkol.wizard.store.auth.jwt.JwtConfig;
import com.codewithsakkol.wizard.store.auth.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;
    private final JwtConfig jwtConfig;
    private final HttpCookieOAuth2AuthorizationRequestRepository oauth2repository;
    private final AuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void  onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){
        // Find Frontend URI we temporarily store in the cookie
        Optional<String> redirectUri = CookieUtils.getCookie(
                request,
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // Fallback frontend URL if none provided
        String targetUrl = redirectUri.orElse("http://localhost:3000/oauth2/redirect");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String roleStr = userDetails.getAuthorities()
                .iterator().next().getAuthority()
                .replace("ROLE_", "");
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userDetails.getName());
        claims.put("email", userDetails.getEmail());
        claims.put("role", roleStr);

        // Generate Token
        String accessToken = jwtUtils.generateAccessToken(userDetails.getId(), claims);
        String refeshToken = jwtUtils.generateRefreshToken(userDetails.getId(), claims);


        // Set refresh token securely in Cookie
        CookieUtils.addCookie(response,
                "refreshToken",
                refeshToken,
                (int) (jwtConfig.getRefreshTokenExpiration() /1000));
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequest(request, response);
    }

}
