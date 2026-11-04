package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResult;
import org.example.dto.auth.LogoutResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.model.employee.EmployeeAccount;
import org.example.service.employee.AuthEmployeeService;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing employee authentication operations,
 * including registration, login, and session status verification.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthEmployeeController {
  private final AuthEmployeeService authEmployeeService;
  private final JwtService jwtService;
  private final RedisSessionService sessionService;

  private static final String MESSAGE_KEY = "message";
  private static final String TOKEN = "accessToken";

  @Value("${app.security.cookie.secure:false}")
  private boolean isCookieSecure;


  /**
   * Registers a new employee account in the system.
   *
   * @param request the registration request body containing employee credentials and role details
   * @param adminId the administrator ID retrieved from the request header
   * @return ResponseEntity with a success message or bad request error description
   */
  @PostMapping("/register-employee")
  public ResponseEntity<String> registerEmployeesAccount(
      @Valid @RequestBody RegisterRequest request, @RequestHeader("X-Admin-id") String adminId) {
    try {
      EmployeeAccount account = new EmployeeAccount();
      account.setEmployeeId(request.getEmployeeId());
      account.setUsername(request.getUsername());
      account.setPassword(request.getPassword());
      account.setRoleId(request.getRoleId());
      authEmployeeService.registerEmployeesAccount(account, adminId);
      return ResponseEntity.ok("EmployeeAccountMessage.REGISTER_SUCCESS.getDesc()");
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    }
  }

  /**
   * Authenticates an employee and sets an HTTP-only secure cookie containing the access token.
   *
   * @param request  the login credentials containing username and password
   * @param response the HTTP response used to add the access token cookie
   * @return a response entity containing success message or unauthorized error if credentials fail
   */
  @PostMapping("/login-employee")
  public ResponseEntity<Map<String, String>> login(
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response) {

    try {
      LoginResult result = authEmployeeService.adminLogin(
          request.getUsername(),
          request.getPassword()
      );

      ResponseCookie cookie = ResponseCookie
          .from(TOKEN, result.getToken())
          .httpOnly(true)
          .secure(isCookieSecure)
          .path("/")
          .sameSite("Lax")
          .maxAge(Duration.ofMinutes(30))
          .build();

      response.addHeader(
          HttpHeaders.SET_COOKIE,
          cookie.toString()
      );

      return ResponseEntity.ok(
          Map.of(MESSAGE_KEY, "Login sukses!")
      );

    } catch (BadCredentialsException e) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(
              MESSAGE_KEY,
              "Username atau password salah"
          ));
    }
  }

  /**
   * Logs out the authenticated employee by invalidating their session and clearing
   * the access token cookie.
   *
   * @param authentication the current authentication principal
   * @param response       the HTTP response used to expire the token cookie
   * @return a response entity indicating successful logout or unauthorized status if not logged in
   */
  @PostMapping("logout")
  ResponseEntity<LogoutResponse> logout(
      Authentication authentication,
      HttpServletResponse response
  ) {
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      ResponseCookie expiredCookie = ResponseCookie
          .from(TOKEN, "")
          .httpOnly(true)
          .secure(isCookieSecure)
          .path("/")
          .sameSite("Lax")
          .maxAge(Duration.ZERO)
          .build();

      response.addHeader(HttpHeaders.SET_COOKIE,
          expiredCookie.toString()
      );

      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new LogoutResponse("Belum login"));
    }

    String username = authentication.getName();
    authEmployeeService.adminLogout(username);

    ResponseCookie expiredCookie = ResponseCookie
        .from(TOKEN, "")
        .httpOnly(true)
        .secure(isCookieSecure)
        .path("/")
        .sameSite("Lax")
        .maxAge(Duration.ZERO)
        .build();

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        expiredCookie.toString()
    );
    log.info("Logout berhasil untuk username: {}", username);

    return ResponseEntity.ok(new LogoutResponse("Logout berhasil"));
  }

  /**
   * Verifies the current authentication status of the user session.
   *
   * @param authentication the Spring Security authentication object of the current request
   * @return ResponseEntity indicating whether the session is active or unauthorized
   */

  @GetMapping("/status")
  public ResponseEntity<Object> checkAuth(
      Authentication authentication,
      HttpServletRequest request) {

    log.info("[AUTH STATUS] Request masuk: {}", request.getRequestURI());

    if (authentication == null) {
      log.warn("[AUTH STATUS] Authentication = null");
    } else {
      log.info("[AUTH STATUS] Authenticated = {}",
          authentication.isAuthenticated());

      log.info("[AUTH STATUS] Tipe Authentication = {}",
          authentication
              .getClass()
              .getSimpleName());

      log.debug("[AUTH STATUS] Principal type = {}",
          authentication.getPrincipal() == null
              ? "null"
              : authentication
              .getPrincipal()
              .getClass()
              .getSimpleName());
    }

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {

      log.warn("[AUTH STATUS] Akses ditolak: belum terautentikasi");

      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(MESSAGE_KEY, "Belum Login"));
    }

    log.info("[AUTH STATUS] Autentikasi berhasil");

    return ResponseEntity.ok(
        Map.of(MESSAGE_KEY, "Session Active")
    );
  }

}