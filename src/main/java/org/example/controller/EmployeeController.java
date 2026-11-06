package org.example.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.ShowEmployeeResponse;
import org.example.model.employee.Employee;
import org.example.service.employee.EmployeeManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Employee Management Controller.
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
  private final EmployeeManagementService employeeManagementService;

  /**
   * Adds a new employee record to the system.
   *
   * <p>This method receives employee data from the request body and the admin ID
   * performing the action from the HTTP header (<code>X-Admin-id</code>)
   * for auditing or authorization verification purposes.</p>
   *
   * @param request the {@link AddEmployeeRequest} object containing the new employee details
   *                to be saved.
   * @return a {@link ResponseEntity} with HTTP status 200 (OK) and a success message
   *      if the creation succeeds, or HTTP status 400 (Bad Request)
   *      with the corresponding error message if validation or business rules fail.
   */
  @PostMapping("/add-employee")
  public ResponseEntity<AddEmployeeResponse> addEmployee(
      @Valid @RequestBody AddEmployeeRequest request) {

    log.info("Received request to add employee");

    try {
      log.debug("Employee request data: {}", request);

      AddEmployeeResponse response =
          employeeManagementService.addEmployees(request);

      log.info("Employee successfully added");

      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(response);

    } catch (Exception e) {
      log.error("Failed to add employee. Error: {}", e.getMessage(), e);
      throw e;
    }
  }

  /**
   * Retrieves a list of all employee records from the system.
   *
   * <p>This method utilizes the security context via the {@link Authentication} object
   * to automatically extract the username of the currently logged-in user
   * for authorization and tracking purposes.</p>
   *
   * @return A {@link ResponseEntity} containing a list of {@link Employee} objects
   *      with HTTP status 200 (OK) if successful, or HTTP status 400 (Bad Request)
   *      with an error message if an illegal state occurs.
   */
  @GetMapping("/show-employee")
  public ResponseEntity<Object> getAllEmployees() {
    List<ShowEmployeeResponse> employees = employeeManagementService.getAllEmployees();

    return ResponseEntity.ok(employees);
  }

  /**
   * Retrieves specific employee details based on the provided employee ID.
   *
   * <p>This method fetches a single employee record using the given {@code employeeId} path
   * variable
   * and validates the requester's authorization using the admin ID from the HTTP header
   * (<code>X-Admin-Id</code>).</p>
   *
   * @param employeeId The unique identifier of the employee to be retrieved,
   *                   passed as a path variable.
   * @param adminId    The ID of the admin requesting the data,
   *                   extracted from the {@code X-Admin-Id} request header.
   * @return A {@link ResponseEntity} containing the found {@link Employee} object
   *      with HTTP status 200 (OK) if successful, or HTTP status 400 (Bad Request)
   *      with an error message if the employee is not found
   *      or an illegal argument occurs.
   */
  @GetMapping("/{employeeId}")
  public ResponseEntity<Object> getEmployeeId(
      @PathVariable String employeeId,
      @RequestHeader("X-Admin-Id") String adminId) {
    try {
      Employee employee = employeeManagementService.getEmployeeById(employeeId);
      return ResponseEntity.ok(employee);
    } catch (IllegalArgumentException e) {
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    }
  }

  /**
   * Retrieves the profile information of the currently authenticated employee.
   *
   * <p>This method extracts the username of the active session from the security context,
   * then queries the database to fetch the corresponding employee profile data.</p>
   *
   * @param authentication the HTTP servlet request object associated with the current call.
   * @return a {@link ResponseEntity} containing the employee's profile data
   *      with HTTP status 200 (OK) if successful, or HTTP status 401 (Unauthorized)
   *      with a session expiration message if an exception
   *      or authentication error occurs.
   */
  @GetMapping("/profile")
  public ResponseEntity<Object> getMyProfile(
      Authentication authentication) {

    String currentUsername = authentication.getName();

    Employee employee =
        employeeManagementService.getEmployeeByUsername(
            currentUsername
        );

    return ResponseEntity.ok(employee);
  }
}
