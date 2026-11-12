package org.example.dto.auth;

import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for returning new employee registration response payloads.
 * Contains the registered employee's credentials and assigned details.
 */
@Getter
@Builder
public class RegisterResponse {
  private String employeeId;
  private String username;
  private String password;
  private String roleId;
}
