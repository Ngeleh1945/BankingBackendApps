package org.example.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for managing and formatting application-wide exceptions
 * into standardized JSON error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String MESSAGE = "message";
  private static final String STATUS = "status";

  /**
   * Handles illegal state exceptions and returns a conflict status response.
   *
   * @param e the illegal state exception thrown
   * @return a ResponseEntity containing the conflict status and error details
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalState(
      IllegalStateException e) {

    log.warn("Illegal state: {}", e.getMessage(), e);

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(Map.of(
            STATUS, HttpStatus.CONFLICT.value(),
            MESSAGE, e.getMessage()
        ));
  }

  /**
   * Handles general unexpected application exceptions
   * and returns an internal server error response.
   *
   * @param e the general exception thrown
   * @return a ResponseEntity containing the internal server error status and a generic message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneral(
      Exception e) {

    log.error("Unexpected application error", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of(
            STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            MESSAGE, "An unexpected internal server error occurred."
        ));
  }

  /**
   * Handles authentication failures due to bad credentials
   * and returns an unauthorized status response.
   *
   * @param e the bad credentials exception thrown
   * @return a ResponseEntity containing the unauthorized message
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentials(
      @NotNull BadCredentialsException e) {

    log.warn("Authentication failed: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(Map.of(
            MESSAGE,
            "Invalid username or password"
        ));
  }

  /**
   * Handles method argument validation failures and returns a bad request status response
   * with the specific validation error message.
   *
   * @param e the method argument not valid exception thrown
   * @return a ResponseEntity containing the bad request status and validation message
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      @NotNull MethodArgumentNotValidException e) {

    String message = e
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElse("Invalid request");

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of(
            STATUS, HttpStatus.BAD_REQUEST.value(),
            MESSAGE, message
        ));
  }
}