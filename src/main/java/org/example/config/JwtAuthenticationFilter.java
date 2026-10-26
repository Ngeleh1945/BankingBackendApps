package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.example.service.employee.AuthEmployeeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT Authentication Filter for processing token validation and security context setup.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final AuthEmployeeService authEmployeeService;
  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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

    if (token != null && !token.isBlank()) {
      try {
        String username = authEmployeeService.extractUsernameFromToken(token);

        if (username != null && SecurityContextHolder
            .getContext()
            .getAuthentication() == null) {
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
          authenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder
              .getContext()
              .setAuthentication(authenticationToken);
        }
      } catch (Exception e) {
        log.warn("JWT tidak valid: {}", e.getMessage(), e);
      }
    }
    filterChain.doFilter(request, response);
  }
}
