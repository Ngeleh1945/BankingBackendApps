package org.example.model.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents employees personal identity and profile data.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

  @Id
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