package org.example.service.employee;

import java.util.List;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.dto.employee.GetEmployeeByIdResponse;
import org.example.dto.employee.ShowEmployeeResponse;

/**
 * Service interface for managing employee data.
 */
public interface EmployeeManagementService {
  /**
   * Adds a new employee to the system.
   *
   * @param request employee the employee details to add
   */
  AddEmployeeResponse addEmployees(AddEmployeeRequest request);

  /**
   * Retrieves a list of all employees.
   *
   * @return the list of employees
   */
  List<ShowEmployeeResponse> getAllEmployees();

  /**
   * Retrieves employee details by their unique identifier.
   *
   * @param employeeId the unique identifier of the employee
   * @return the Employee instance if found
   */
  GetEmployeeByIdResponse getEmployeeById(String employeeId);

  /**
   * Retrieves employee details by their associated account username.
   *
   * @param username the username associated with the employee account
   * @return the Employee instance if found
   */
  ShowEmployeeResponse getEmployeeByUsername(String username);

  /**
   * Deletes an employee record from the system based on their unique identifier.
   *
   * @param employeeId the unique identifier of the employee to be deleted
   */
  void deleteEmployees(String employeeId);

  /**
   * Updates an existing employee's details with the provided request data.
   *
   * @param employeeId the unique identifier of the employee to be updated
   * @param request    the DTO containing the updated employee information
   * @return an {@link AddEmployeeResponse} containing the updated employee details
   */
  AddEmployeeResponse update(String employeeId, AddEmployeeRequest request);
}