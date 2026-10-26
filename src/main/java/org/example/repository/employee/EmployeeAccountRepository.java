package org.example.repository.employee;

import java.util.List;
import java.util.Optional;
import org.example.model.employee.EmployeeAccount;

/**
 * Repository Interface Employee Account managing Authentication Administration Employees.
 */
public interface EmployeeAccountRepository {
  /**
   * Saves or updates an employee account entity.
   *
   * @param employeeId the employee account entity to save or update
   * @return the saved employee account instance
   */
  EmployeeAccount save(EmployeeAccount employeeId);

  /**
   * Finds an employee account by its username.
   *
   * @param username the username to search for
   * @return an Optional containing the EmployeeAccount if found, or empty otherwise
   */
  Optional<EmployeeAccount> findByUsername(String username);

  /**
   * Retrieves all employee account records from the database.
   *
   * @return a list of all employee accounts
   */
  List<EmployeeAccount> findAll();

  /**
   * Deletes an employee account record by its identifier.
   *
   * @param employee the employee identifier to delete
   */
  void delete(String employee);
}
