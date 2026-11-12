package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for handling new employee registration request payloads.
 * Contains login credential information and role assignment details.
 */
@Getter
@Setter
@Builder
public class RegisterRequest {

  private String employeeId;

  /**
   * Unique username for system authentication.
   * Must not be blank or empty.
   */
  @NotBlank(message = "Username cannot be blank!")
  private String username;

  /**
   * User account password.
   * Required and will be encrypted using a password encoder in the service layer.
   */
  @NotBlank(message = "Password cannot be blank!")
  private String password;

  /**
   * Role identifier (Role ID) assigned to the new user.
   * Determines authorization boundaries within the application module.
   */
  @NotBlank(message = "Role id cannot be blank!")
  private String roleId;
}
