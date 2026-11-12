package org.example.service.employee;

import java.util.List;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.GetEmployeeByIdResponse;
import org.example.dto.employee.ShowEmployeeResponse;
import org.example.model.employee.Employee;

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
   *
   * @param employeeId
   */
  void deleteEmployees(String employeeId);

  /**
   *
   * @param employeeId
   * @param request
   * @return
   */
  AddEmployeeResponse update(String employeeId, AddEmployeeRequest request);
}