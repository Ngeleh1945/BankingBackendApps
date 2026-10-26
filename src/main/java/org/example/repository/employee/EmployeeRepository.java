package org.example.repository.employee;

import java.util.List;
import java.util.Optional;
import org.example.model.employee.Employee;

/**
 * Repository Employee for managing employee data.
 */
public interface EmployeeRepository {
  /**
   * Saves or updates an employee entity.
   *
   * @param employee the employee entity to save or update
   * @return the saved employee instance
   */
  Employee save(Employee employee);

  /**
   * Finds an employee by its unique identifier.
   *
   * @param employeeId the employee identifier to search for
   * @return an Optional containing the Employee if found, or empty otherwise
   */
  Optional<Employee> findById(String employeeId);

  /**
   * Retrieves all employee records from the database.
   *
   * @return a list of all employees
   */
  List<Employee> findAll();

  /**
   * Deletes an employee record by its identifier.
   *
   * @param employeeId the employee identifier to delete
   */
  void delete(String employeeId);
}