package org.example.service.employee;

import java.util.List;
import org.example.model.employee.Employee;

/**
 * Service interface for managing employee data.
 */
public interface EmployeeManagementService {
  /**
   * Adds a new employee to the system.
   *
   * @param employee the employee details to add
   * @param adminId  the identifier of the admin performing the action
   */
  void addEmployees(Employee employee, String adminId);

  /**
   * Retrieves a list of all employees.
   *
   * @param adminId the identifier of the admin requesting the data
   * @return the list of employees
   */
  List<Employee> getAllEmployees(String adminId);

  /**
   * Retrieves employee details by their unique identifier.
   *
   * @param employeeId the unique identifier of the employee
   * @return the Employee instance if found
   */
  Employee getEmployeeById(String employeeId);

  /**
   * Retrieves employee details by their associated account username.
   *
   * @param username the username associated with the employee account
   * @return the Employee instance if found
   */
  Employee getEmployeeByUsername(String username);
}