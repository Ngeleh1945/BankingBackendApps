package org.example.exception;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String MESSAGE = "message";

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalState(
      IllegalStateException e) {

    log.warn("Illegal state: {}", e.getMessage(), e);

    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(Map.of(
            "status", HttpStatus.CONFLICT.value(),
            MESSAGE, e.getMessage()
        ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneral(
      Exception e) {

    log.error("Unexpected application error", e);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of(
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            MESSAGE, "An unexpected internal server error occurred."
        ));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleBadCredentials(
      BadCredentialsException e) {

    log.warn("Authentication failed: {}", e.getMessage());

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(Map.of(
            MESSAGE,
            "Invalid username or password"
        ));
  }
}