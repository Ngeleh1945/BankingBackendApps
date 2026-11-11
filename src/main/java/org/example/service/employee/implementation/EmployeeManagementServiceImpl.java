package org.example.service.employee.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.employee.AddEmployeeRequest;
import org.example.dto.employee.AddEmployeeResponse;
import org.example.dto.employee.GetEmployeeByIdResponse;
import org.example.dto.employee.ShowEmployeeResponse;
import org.example.enums.EmployeeMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.EmployeeManagementService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
  private final EmployeeRepository repository;
  private final EmployeeAccountRepository employeeAccountRepository;

  @Override
  @Transactional
  public AddEmployeeResponse addEmployees(AddEmployeeRequest request) {

    String nik = request.getNik();

    if (repository
        .findByNik(nik)
        .isPresent()) {
      throw new IllegalStateException(
          String.format(
              EmployeeMessage.ALREADY_EXIST.getDesc(),
              nik
          )
      );
    }

    Employee employee = new Employee();
    employee.setEmployeeId(generateId());
    employee.setNik(nik);
    employee.setFullName(request.getFullName());
    employee.setGender(request.getGender());
    employee.setPlaceOfBirth(request.getPlaceOfBirth());
    employee.setDateOfBirth(request.getDateOfBirth());
    employee.setAddress(request.getAddress());
    employee.setPhoneNumber(request.getPhoneNumber());
    employee.setPosition(request.getPosition());
    employee.setBankNames(request.getBankNames());
    employee.setAccountNumber(request.getAccountNumber());
    employee.setNpwp(request.getNpwp());

    repository.save(employee);

    return AddEmployeeResponse
        .builder()
        .employeeId(employee.getEmployeeId())
        .nik(employee.getNik())
        .fullName(employee.getFullName())
        .gender(employee.getGender())
        .placeOfBirth(employee.getPlaceOfBirth())
        .dateOfBirth(employee.getDateOfBirth())
        .address(employee.getAddress())
        .phoneNumber(employee.getPhoneNumber())
        .position(employee.getPosition())
        .bankNames(employee.getBankNames())
        .accountNumber(employee.getAccountNumber())
        .npwp(employee.getNpwp())
        .build();
  }

  @Override
  public List<ShowEmployeeResponse> getAllEmployees() {
    return repository
        .findAll()
        .stream()
        .map(banks -> ShowEmployeeResponse
            .builder()
            .employeeId(banks.getEmployeeId())
            .fullName(banks.getFullName())
            .gender(banks.getGender())
            .position(banks.getPosition())
            .build())
        .toList();
  }

  @Override
  public GetEmployeeByIdResponse getEmployeeById(String employeeId) {
    Employee employee = repository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Employee search failed: ID {} not found", employeeId);
          return new IllegalArgumentException(
              messageNotFound(employeeId));
        });

    return GetEmployeeByIdResponse
        .builder()
        .employeeId(employee.getEmployeeId())
        .nik(employee.getNik())
        .fullName(employee.getFullName())
        .gender(employee.getGender())
        .placeOfBirth(employee.getPlaceOfBirth())
        .dateOfBirth(employee.getDateOfBirth())
        .address(employee.getAddress())
        .phoneNumber(employee.getPhoneNumber())
        .position(employee.getPosition())
        .bankNames(employee.getBankNames())
        .accountNumber(employee.getAccountNumber())
        .npwp(employee.getNpwp())
        .build();
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
    return repository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Physical employee data not found for ID: {}", employeeId);
          return new IllegalArgumentException(
              messageNotFound(employeeId));
        });
  }

  @Override
  @Transactional
  public void deleteEmployees(String employeeId) {
    Employee employee = repository
        .findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            messageNotFound(employeeId)
        ));

    repository.delete(employee);
  }

  @Override
  @Transactional
  public AddEmployeeResponse update(
      String employeeId,
      AddEmployeeRequest request
  ) {
    Employee employee = repository
        .findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            messageNotFound(employeeId)
        ));

    employee.setNik(request.getNik());
    employee.setFullName(request.getFullName());
    employee.setGender(request.getGender());
    employee.setPlaceOfBirth(request.getPlaceOfBirth());
    employee.setDateOfBirth(request.getDateOfBirth());
    employee.setAddress(request.getAddress());
    employee.setPhoneNumber(request.getPhoneNumber());
    employee.setPosition(request.getPosition());
    employee.setBankNames(request.getBankNames());
    employee.setAccountNumber(request.getAccountNumber());
    employee.setNpwp(request.getNpwp());

    Employee updatedEmployee = repository.save(employee);

    return AddEmployeeResponse
        .builder()
        .nik(updatedEmployee.getNik())
        .fullName(updatedEmployee.getFullName())
        .gender(updatedEmployee.getGender())
        .placeOfBirth(updatedEmployee.getPlaceOfBirth())
        .dateOfBirth(updatedEmployee.getDateOfBirth())
        .address(updatedEmployee.getAddress())
        .phoneNumber(updatedEmployee.getPhoneNumber())
        .position(updatedEmployee.getPosition())
        .bankNames(updatedEmployee.getBankNames())
        .accountNumber(updatedEmployee.getAccountNumber())
        .npwp(updatedEmployee.getNpwp())
        .build();
  }

  private @NotNull String generateId() {
    String prefix = "BRM";

    int lastNumber = repository
        .findAll()
        .stream()
        .map(Employee::getEmployeeId)
        .filter(id -> id != null && id.matches("^BRM\\d+$"))
        .mapToInt(id -> Integer.parseInt(id.substring(prefix.length())))
        .max()
        .orElse(0);
    return prefix + (lastNumber + 1);
  }

  private String messageNotFound(String employeeId) {
    return "Employee with ID " + employeeId + " not found";
  }
}