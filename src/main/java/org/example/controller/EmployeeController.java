package org.example.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.common.ApiResponse;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.dto.employee.GetEmployeeByIdResponse;
import org.example.dto.employee.ShowEmployeeResponse;
import org.example.model.employee.Employee;
import org.example.service.employee.EmployeeManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
   * <p>This method receives validated employee registration details from the request body,
   * processes the creation through the org.example.service layer,
   * and logs the execution lifecycle.</p>
   *
   * @param request the {@link AddEmployeeRequest} object containing the new employee data.
   * @return a {@link ResponseEntity} containing the {@link AddEmployeeResponse} and
   *      HTTP status 201 (Created) upon successful persistence.
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
   * Retrieves a list of all employee records registered in the system.
   *
   * @return a {@link ResponseEntity} containing a list of {@link ShowEmployeeResponse} objects
   *      with HTTP status 200 (OK).
   */
  @GetMapping("/show-employee")
  public ResponseEntity<Object> getAllEmployees() {
    List<ShowEmployeeResponse> employees = employeeManagementService.getAllEmployees();

    return ResponseEntity.ok(employees);
  }

  /**
   * Retrieves specific employee details based on the provided employee ID.
   *
   * <p>This method queries a single employee record using the specified path variable.</p>
   *
   * @param employeeId the unique identifier of the target employee.
   * @return a {@link ResponseEntity} containing the found {@link Employee} object with
   *      HTTP status 200 (OK), or HTTP status 400 (Bad Request) if validation or lookup fails.
   */
  @GetMapping("/{employeeId}")
  public ResponseEntity<ApiResponse<GetEmployeeByIdResponse>> getEmployeeById(
      @PathVariable String employeeId
  ) {
    GetEmployeeByIdResponse response =
        employeeManagementService.getEmployeeById(employeeId);

    return ResponseEntity.ok(
        ApiResponse.<GetEmployeeByIdResponse>builder()
            .success(true)
            .message("Employee successfully retrieved")
            .data(response)
            .build()
    );
  }

  /**
   * Retrieves the profile information of the currently authenticated employee.
   *
   * <p>This method extracts the active username from the Spring Security context
   * to fetch the corresponding employee record.</p>
   *
   * @param authentication the current security authentication object containing session details.
   * @return a {@link ResponseEntity} containing the {@link Employee} profile data
   *      with HTTP status 200 (OK).
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

  /**
   * Deletes an employee record by their unique identifier.
   *
   * @param employeeId the unique identifier of the employee to be deleted
   * @return a ResponseEntity containing the success status and response message
   */
  @DeleteMapping("/delete/{employeeId}")
  public ResponseEntity<ApiResponse<Void>> deleteEmployeeById(
      @PathVariable String employeeId
  ) {
    employeeManagementService.deleteEmployees(employeeId);

    return ResponseEntity.ok(
        ApiResponse
            .<Void>builder()
            .success(true)
            .message("Employee successfully deleted")
            .data(null)
            .build()
    );
  }

  /**
   * Updates an existing employee record identified by their unique identifier.
   *
   * @param employeeId the unique identifier of the employee to be updated
   * @param request    the payload containing the updated employee details
   * @return a ResponseEntity containing the API response structure, success status,
   *      update confirmation message, and the updated employee data.
   */
  @PutMapping("/update/{employeeId}")
  public ResponseEntity<ApiResponse<AddEmployeeResponse>> updateEmployeeById(
      @PathVariable String employeeId,
      @Valid @RequestBody AddEmployeeRequest request
  ) {

    AddEmployeeResponse response = employeeManagementService.update(
        employeeId,
        request
    );
    return ResponseEntity.ok(
        ApiResponse
            .<AddEmployeeResponse>builder()
            .success(true)
            .message("Employee successfully updated")
            .data(response)
            .build()
    );
  }
}
