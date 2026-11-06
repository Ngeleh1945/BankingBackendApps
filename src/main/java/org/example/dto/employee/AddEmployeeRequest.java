package org.example.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
  @NotBlank(message = "NIK cannot be blank")
  private String nik;
  @NotBlank(message = "Name cannot be blank")
  private String fullName;
  @NotBlank(message = "Place of birth cannot be blank")
  private String placeOfBirth;
  @NotNull(message = "Date of birth cannot be null")
  private LocalDate dateOfBirth;
  @NotBlank(message = "address cannot be blank")
  private String address;
  @NotBlank(message = "Phone number cannot be blank")
  private String phoneNumber;
  @NotBlank(message = "position cannot be blank")
  private String position;
  @NotBlank(message = "Bank name cannot be blank")
  private String bankNames;
  @NotBlank(message = "Account Number cannot be blank")
  private String accountNumber;
  @NotBlank(message = "NPWP cannot be blank")
  private String npwp;
}
