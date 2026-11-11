
package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Custom JWT authentication filter executing once per HTTP request (OncePerRequestFilter).
 * Responsible for extracting the token from an HttpOnly cookie, validating the session existence
 * in Redis, dynamically refreshing the session expiration time (sliding expiration), and populating
 * the SecurityContext if authentication is valid.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String COOKIE_NAME = "accessToken";

  private final JwtService jwtService;
  private final RedisSessionService sessionService;

  @Value("${app.security.cookie.secure:false}")
  private boolean cookieSecure;

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = extractToken(request);

    if (isTokenPresent(token)) {
      authenticateRequest(token, request, response);
    }

    filterChain.doFilter(request, response);
  }

  private @Nullable String extractToken(
      @NotNull HttpServletRequest request) {

    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if (COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }

  private boolean isTokenPresent(String token) {
    return token != null && !token.isBlank();
  }

  private void authenticateRequest(
      String token,
      HttpServletRequest request,
      HttpServletResponse response) {

    try {
      String username =
          jwtService.extractUsernameFromToken(token);

      String sessionId =
          jwtService.extractSessionIdFromToken(token);

      if (username == null || username.isBlank()
          || sessionId == null || sessionId.isBlank()) {

        log.warn("[JWT FILTER] Invalid username or sessionId");
        return;
      }

      validateSessionAndAuthenticate(
          username,
          sessionId,
          request,
          response
      );

    } catch (Exception e) {
      log.warn(
          "[JWT FILTER] Token validation failed: {}",
          e.getClass().getSimpleName()
      );

      SecurityContextHolder.clearContext();
    }
  }

  private void validateSessionAndAuthenticate(
      String username,
      String sessionId,
      HttpServletRequest request,
      HttpServletResponse response) {

    boolean sessionValid =
        sessionService.validateAndRefreshSession(
            username,
            sessionId
        );

    if (!sessionValid) {
      log.warn(
          "[JWT FILTER] Invalid Redis session: {}",
          username
      );
      return;
    }

    if (SecurityContextHolder.getContext()
        .getAuthentication() != null) {
      return;
    }

    String refreshedToken =
        jwtService.generateToken(username, sessionId);

    refreshAuthCookie(refreshedToken, response);

    setAuthentication(username, request);
  }

  private void refreshAuthCookie(
      String token,
      HttpServletResponse response) {

    ResponseCookie cookie = ResponseCookie
        .from(COOKIE_NAME, token)
        .httpOnly(true)
        .secure(cookieSecure)
        .path("/")
        .sameSite("Lax")
        .maxAge(Duration.ofMinutes(30))
        .build();

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        cookie.toString()
    );
  }

  private void setAuthentication(
      String username,
      HttpServletRequest request) {

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            username,
            null,
            Collections.emptyList()
        );

    authentication.setDetails(
        new WebAuthenticationDetailsSource()
            .buildDetails(request)
    );

    SecurityContextHolder.getContext()
        .setAuthentication(authentication);

    log.info("[JWT FILTER] SecurityContext successfully populated");
  }
}