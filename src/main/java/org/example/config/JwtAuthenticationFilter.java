package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Authentication Filter for processing token validation
 * and security context setup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final RedisSessionService sessionService;

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String token = extractToken(request);

    if (isTokenPresent(token)) {
      authenticateRequest(token, request);
    }

    filterChain.doFilter(request, response);
  }

  private @Nullable String extractToken(@NotNull HttpServletRequest request) {

    if (request.getCookies() == null) {
      return null;
    }

    for (Cookie cookie : request.getCookies()) {
      if ("accessToken".equals(cookie.getName())) {
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
      @NotNull HttpServletRequest request) {

    log.info(
        "[JWT FILTER] Token ditemukan: {}",
        request.getRequestURI()
    );

    try {
      String username = jwtService.extractUsernameFromToken(token);

      if (username == null || username.isBlank()) {
        log.warn("[JWT FILTER] Username tidak ditemukan");
        return;
      }

      validateSessionAndAuthenticate(username, token, request);

    } catch (Exception e) {
      log.error(
          "[JWT FILTER] Validasi token gagal: {}",
          e
              .getClass()
              .getSimpleName(),
          e
      );

      SecurityContextHolder.clearContext();
    }
  }

  private void validateSessionAndAuthenticate(
      String username,
      String token,
      HttpServletRequest request) {

    boolean sessionValid =
        sessionService.validateAndRefreshSession(username, token);

    if (!sessionValid) {
      log.warn(
          "[JWT FILTER] Redis session tidak valid: {}",
          username
      );
      return;
    }

    if (SecurityContextHolder
        .getContext()
        .getAuthentication() != null) {
      return;
    }

    setAuthentication(username, request);
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
        new WebAuthenticationDetailsSource().buildDetails(request)
    );

    SecurityContextHolder
        .getContext()
        .setAuthentication(authentication);

    log.info(
        "[JWT FILTER] SecurityContext berhasil diisi"
    );
  }
}