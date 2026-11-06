package org.example.dto.employee;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddEmployeeResponse {
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
}
