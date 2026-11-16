package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResult;
import org.example.dto.auth.LogoutResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.dto.auth.ShowAccountEmployeeResponse;
import org.example.dto.common.ApiResponse;
import org.example.model.employee.EmployeeAccount;
import org.example.service.employee.AuthEmployeeService;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
   * @return ResponseEntity with a success message or bad request error description
   */
  @PostMapping("/register-account")
  public ResponseEntity<ApiResponse<RegisterResponse>> registerEmployeesAccount(
      @Valid @RequestBody RegisterRequest request) {
    RegisterResponse response = authEmployeeService.registerEmployeesAccount(request);

    ApiResponse<RegisterResponse> apiResponse = ApiResponse.<RegisterResponse>builder()
        .success(true)
        .message("Employee account registered successfully")
        .data(response)
        .build();

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(apiResponse);
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
        Map.of(MESSAGE_KEY, "Login success!")
    );
  }

  /**
   * Retrieves a comprehensive list of all registered employee accounts.
   * Wraps the retrieved list of employee account profiles within a standardized API response.
   *
   * @return a {@link ResponseEntity} containing an {@link ApiResponse} with a list of
   *     {@link ShowAccountEmployeeResponse} objects
   */
  @GetMapping("/show-accounts")
  public ResponseEntity<ApiResponse<List<ShowAccountEmployeeResponse>>> getAllAccounts() {
    List<ShowAccountEmployeeResponse> employeeAccounts =
        authEmployeeService.showAccounts();

    ApiResponse<List<ShowAccountEmployeeResponse>> apiResponse = ApiResponse
        .<List<ShowAccountEmployeeResponse>>builder()
        .success(true)
        .message("Employee accounts successfully retrieved")
        .data(employeeAccounts)
        .build();

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * Retrieves an employee account by its unique employee identifier.
   *
   * @param employeeId the unique identifier of the employee whose account is being requested
   * @return a {@link ResponseEntity} containing a standard {@link ApiResponse}
   *         with the retrieved {@link EmployeeAccount} data
   */
  @GetMapping("/account-{employeeId}")
  public ResponseEntity<ApiResponse<EmployeeAccount>> getAccountById(
      @PathVariable String employeeId
  ) {
    EmployeeAccount response = authEmployeeService.getAccountById(employeeId);

    ApiResponse<EmployeeAccount> apiResponse = ApiResponse.<EmployeeAccount>builder()
        .success(true)
        .message("Account successfully retrieved")
        .data(response)
        .build();

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * Updates the role assigned to a specific employee account.
   *
   * @param employeeId     the unique identifier of the employee whose account role is being updated
   * @param request        the request payload containing the new role details
   * @param authentication the current security authentication context
   *                       of the user performing the request
   * @return a {@link ResponseEntity} containing a standard {@link ApiResponse}
   *         with the updated {@link EmployeeAccount} data
   */
  @PutMapping("/update/{employeeId}/role")
  public ResponseEntity<ApiResponse<EmployeeAccount>> updateRole(
      @PathVariable String employeeId,
      @Valid @RequestBody EmployeeAccount request,
      @NotNull Authentication authentication
  ) {
    EmployeeAccount response = authEmployeeService.updateRole(employeeId, request, authentication);

    ApiResponse<EmployeeAccount> apiResponse = ApiResponse.<EmployeeAccount>builder()
        .success(true)
        .message("Employee account role updated successfully")
        .data(response)
        .build();

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * Deletes an employee account from the system after verifying that the
   * requesting user has the necessary authorization and permissions.
   *
   * @param employeeId     the unique identifier of the employee whose account is being deleted
   * @param authentication the security context used to verify whether
   *                       the current user is authorized to perform the deletion
   * @return a {@link ResponseEntity} containing a standard
   *      {@link ApiResponse} confirming the deletion
   */
  @DeleteMapping("/delete/account/{employeeId}")
  public ResponseEntity<ApiResponse<Void>> deleteEmployeeAccount(
      @PathVariable String employeeId,
      @NotNull Authentication authentication
  ) {
    authEmployeeService.deleteAccount(employeeId, authentication);

    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .success(true)
            .message("Account successfully deleted")
            .data(null)
            .build()
    );
  }

  /**
   * Logs out the authenticated employee by invalidating their session and clearing
   * the access token cookie.
   *
   * @param authentication the current authentication principal
   * @param response       the HTTP response used to expire the token cookie
   * @return a response entity indicating successful logout or unauthorized status if not logged in
   */
  @PostMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(
      Authentication authentication,
      HttpServletResponse response) {

    if (isAuthenticatedUser(authentication)) {
      String username = authentication.getName();

      authEmployeeService.adminLogout(username);

      log.info("Logout successful for username: {}", username);
    }

    clearAuthCookie(response);

    return ResponseEntity.ok(
        new LogoutResponse("Logout successful")
    );
  }

  /**
   * Verifies the current authentication status of the user session.
   *
   * @param authentication the Spring Security authentication object of the current request
   * @return ResponseEntity indicating whether the session is active or unauthorized
   */

  @GetMapping("/status")
  public ResponseEntity<Map<String, String>> checkAuth(
      Authentication authentication) {

    if (!isAuthenticatedUser(authentication)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(Map.of(
              MESSAGE_KEY,
              "User is not authenticated"
          ));
    }

    return ResponseEntity.ok(
        Map.of(MESSAGE_KEY, "Session Active")
    );
  }

  private boolean isAuthenticatedUser(Authentication authentication) {
    return authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  private void clearAuthCookie(@NotNull HttpServletResponse response) {
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
  }
}