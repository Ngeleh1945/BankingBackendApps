package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.model.employee.EmployeeAccount;
import org.example.service.employee.AuthEmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
public class LoginEmployeeController {
  private final AuthEmployeeService authEmployeeService;

  private static final String MESSAGE_KEY = "message";

  @Value("${app.security.cookie.secure:false}")
  private boolean isCookieSecure;

  // <--- PASTIKAN ADA 1 BARIS KOSONG DI SINI SEBELUM JAVADOC / METHOD

  /**
   * Registers a new employee account in the system.
   *
   * @param request the registration request body containing employee credentials and role details
   * @param adminId the administrator ID retrieved from the request header
   * @return ResponseEntity with a success message or bad request error description
   */
  @PostMapping("/register")
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
   * Authenticates an employee and generates a JWT token stored as an HTTP-only cookie.
   *
   * @param request     the login request body containing username and password
   * @param httpRequest the HTTP servlet request used to check existing session cookies
   * @param response    the HTTP servlet response used to attach the generated JWT cookie
   * @return ResponseEntity containing a success message or unauthorized status on failure
   */
  @PostMapping("/login-employee")
  public ResponseEntity<Object> login(
      @Valid @RequestBody LoginRequest request,
      HttpServletRequest httpRequest,
      HttpServletResponse response) {

    log.info("[LOGIN] Request login diterima");

    if (httpRequest.getCookies() != null) {
      for (Cookie cookie : httpRequest.getCookies()) {
        if ("accessToken".equals(cookie.getName())
            && cookie.getValue() != null
            && !cookie.getValue().isBlank()) {

          log.warn("[LOGIN] Cookie accessToken sudah ada");

          return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(Map.of(
                  MESSAGE_KEY,
                  "Anda sudah login ditempat lain!"
              ));
        }
      }
    }

    boolean isSuccess = authEmployeeService.adminLogin(
        request.getUsername(),
        request.getPassword()
    );

    if (!isSuccess) {
      log.warn("[LOGIN] Autentikasi username/password gagal");

      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(MESSAGE_KEY, "Login gagal!"));
    }

    String token = authEmployeeService.generateToken(
        request.getUsername()
    );

    Cookie jwtCookie = new Cookie("accessToken", token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(isCookieSecure);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(24 * 60 * 60);

    response.addCookie(jwtCookie);

    log.info("[LOGIN] Autentikasi berhasil");
    log.info("[LOGIN] Cookie accessToken ditambahkan ke response");
    log.debug("[LOGIN] Cookie Secure={}", isCookieSecure);

    return ResponseEntity.ok(
        Map.of(MESSAGE_KEY, "Login sukses!")
    );
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
          authentication.getClass().getSimpleName());

      log.debug("[AUTH STATUS] Principal type = {}",
          authentication.getPrincipal() == null
              ? "null"
              : authentication.getPrincipal()
              .getClass().getSimpleName());
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