package org.example.model.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents employees account administration .
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee_accounts")
public class EmployeeAccount {
  @Id
  @Column(name = "employee_id")
  private String employeeId;
  @Column(name = "username")
  private String username;
  @NotBlank(message = "Password tidak boleh kosong")
  @Column(name = "password")
  private String password;
  @Column(name = "role_id")
  private String roleId;
}
