package org.example.model.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_employee_nik",
        columnNames = "nik"
    )
})
public class Employee {

  @Id
  @Column(nullable = false)
  private String employeeId;
  @Column(nullable = false)
  private String nik;
  @Column(nullable = false)
  private String fullName;
  @Column(nullable = false)
  private String gender;
  @Column(nullable = false)
  private String placeOfBirth;
  @Column(nullable = false)
  private LocalDate dateOfBirth;
  @Column(nullable = false)
  private String address;
  @Column(nullable = false)
  private String phoneNumber;
  @Column(nullable = false)
  private String position;
  @Column(nullable = false)
  private String bankNames;
  @Column(nullable = false)
  private String accountNumber;
  @Column(nullable = false)
  private String npwp;
}