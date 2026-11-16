package org.example.dto.auth;

import lombok.Builder;
import lombok.Getter;

/**
 *
 */
@Getter
@Builder
public class ShowAccountEmployeeResponse {
  private String employeeId;
  private String fullName;
  private String username;
  private String position;
  private String description;
}
