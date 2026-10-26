package org.example.model.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents employees personal identity and profile data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
  @Id
  @Column(name = "employee_id")
  private String employeeId;
  @Column(name = "nik")
  private String nik;
  @Column(name = "full_name")
  private String fullName;
  @Column(name = "place_of_birth")
  private String placeOfBirth;
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
  @Column(name = "address")
  private String address;
  @Column(name = "phone_number")
  private String phoneNumber;
  @Column(name = "position")
  private String position;
  @Column(name = "bank_names")
  private String bankNames;
  @Column(name = "account_number")
  private String accountNumber;
  @Column(name = "npwp")
  private String npwp;
  @Column(name = "salary")
  private String salary;
}
