package org.example.service.employee.implementation;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.enums.EmployeeMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.EmployeeManagementService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
  private final AuthEmployeeServiceImpl authEmployeeServiceImpl;
  private final EmployeeRepository employeeRepository;
  private final EmployeeAccountRepository employeeAccountRepository;

  @Override
  @Transactional
  public void addEmployees(AddEmployeeRequest request, String username) {
    if (username == null || username.isBlank()) {
      log.warn("Failed to add employee: Invalid admin session");
      throw new IllegalStateException(
          LoginMessage.INVALIDSESSION.getDesc()
      );
    }

    if (request == null
        || request.getEmployeeId() == null
        || request.getEmployeeId().isBlank()) {
      log.warn("Failed to add employee: Employee data or ID is empty");
      throw new IllegalArgumentException(
          EmployeeMessage.CANNOT_NULL.getDesc()
      );
    }

    String employeeId = request.getEmployeeId();

    if (employeeRepository.findById(employeeId).isPresent()) {
      log.warn("Failed to add employee: ID {} is already registered", employeeId);
      throw new IllegalStateException(
          String.format(
              EmployeeMessage.ALREADY_EXIST.getDesc(),
              employeeId
          )
      );
    }

    Employee employee = new Employee();
    employee.setEmployeeId(employeeId);
    employee.setNik(request.getNik());
    employee.setFullName(request.getFullName());
    employee.setPlaceOfBirth(request.getPlaceOfBirth());
    employee.setDateOfBirth(request.getDateOfBirth());
    employee.setAddress(request.getAddress());
    employee.setPhoneNumber(request.getPhoneNumber());
    employee.setPosition(request.getPosition());
    employee.setBankNames(request.getBankNames());
    employee.setAccountNumber(request.getAccountNumber());
    employee.setNpwp(request.getNpwp());
    employee.setSalary(request.getSalary());

    employeeRepository.save(employee);

    log.info("Admin {} added employee with ID: {}",
        username, employeeId);
  }

  @Override
  public List<Employee> getAllEmployees(String adminId) {
    if (adminId == null || adminId.isBlank()) {
      log.warn("Failed to retrieve employee list: Invalid admin session");
      throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
    }

    log.debug("Fetching all employee data by admin ID: {}", adminId);
    return employeeRepository.findAll();
  }

  @Override
  public Employee getEmployeeById(String employeeId) {
    return employeeRepository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Employee search failed: ID {} not found", employeeId);
          return new IllegalArgumentException(
              "Employee with ID " + employeeId + " not found");
        });
  }

  @Override
  public Employee getEmployeeByUsername(String username) {
    EmployeeAccount account = employeeAccountRepository
        .findByUsername(username)
        .orElseThrow(() -> {
          log.warn("Failed to find employee account: Username {} not found", username);
          return new IllegalArgumentException("Employee with the specified username not found");
        });

    String employeeId = account.getEmployeeId();
    return employeeRepository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Physical employee data not found for ID: {}", employeeId);
          return new IllegalArgumentException(
              "Employee with ID " + employeeId + " not found");
        });
  }
}