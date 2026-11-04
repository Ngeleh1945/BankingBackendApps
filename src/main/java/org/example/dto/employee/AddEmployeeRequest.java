package org.example.dto.employee;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for handling employee registration and creation requests.
 *
 * <p>This class encapsulates the data sent from the client (such as the frontend or Postman)
 * when adding a new employee, shielding the internal database entity from direct exposure.</p>
 */
@Getter
@Setter
@Builder
public class AddEmployeeRequest {
  private String employeeId;
  private String nik;
  private String fullName;
  private String placeOfBirth;
  private LocalDate dateOfBirth;
  private String address;
  private String phoneNumber;
  private String position;
  private String bankNames;
  private String accountNumber;
  private String npwp;
  private String salary;
}
