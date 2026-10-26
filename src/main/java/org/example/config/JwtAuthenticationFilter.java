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
import org.example.service.employee.AuthEmployeeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Authentication Filter for processing token validation and security context setup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final AuthEmployeeService authEmployeeService;

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = null;

    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("accessToken".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    if (token == null || token.isBlank()) {
      log.warn("[JWT FILTER] Token tidak ditemukan: {}", request.getRequestURI());
    } else {
      log.info("[JWT FILTER] Token ditemukan: {}", request.getRequestURI());

      try {
        String username = authEmployeeService.extractUsernameFromToken(token);

        log.info("[JWT FILTER] Token valid");

        if (username != null
            && SecurityContextHolder.getContext().getAuthentication() == null) {

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  username, null, Collections.emptyList()
              );

          authentication.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );

          SecurityContextHolder.getContext().setAuthentication(authentication);

          log.info("[JWT FILTER] SecurityContext berhasil diisi");
        } else {
          log.warn("[JWT FILTER] Username null atau Authentication sudah ada");
        }

      } catch (Exception e) {
        log.error("[JWT FILTER] Validasi token gagal: {}",
            e.getClass().getSimpleName(), e);
      }
    }

    filterChain.doFilter(request, response);
  }
}