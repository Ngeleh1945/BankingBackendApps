package org.example.dto.employee;

import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) for handling show list employee.
 */
@Getter
@Builder
public class ShowEmployeeResponse {
  private String employeeId;
  private String fullName;
  private String position;
}
