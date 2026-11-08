package org.example.repository.employee;

import java.util.Optional;
import org.example.model.employee.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Interface Employee Account managing Authentication Administration Employees.
 */
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, String> {

  /**
   * Finds an employee account by its username.
   *
   * @param username the username to search for
   * @return an Optional containing the EmployeeAccount if found, or empty otherwise
   */
  Optional<EmployeeAccount> findByUsername(String username);
}
