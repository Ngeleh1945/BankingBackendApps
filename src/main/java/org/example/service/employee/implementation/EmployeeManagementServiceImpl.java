package org.example.service.employee.implementation;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public void addEmployees(Employee employee, String adminId) {
    if (adminId == null || adminId.isBlank()) {
      log.warn("Gagal menambah pegawai: Sesi admin tidak valid");
      throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
    }

    if (employee == null || employee.getEmployeeId() == null || employee
        .getEmployeeId()
        .isBlank()) {
      log.warn("Gagal menambah pegawai: Data employee atau ID kosong");
      throw new IllegalArgumentException(EmployeeMessage.CANNOT_NULL.getDesc());
    }

    Optional<Employee> existingEmployee = employeeRepository.findById(employee.getEmployeeId());
    if (existingEmployee.isPresent()) {
      log.warn("Gagal menambah pegawai: ID {} sudah terdaftar", employee.getEmployeeId());
      throw new IllegalStateException(
          String.format(EmployeeMessage.ALREADY_EXIST.getDesc(), employee.getEmployeeId()));
    }

    employeeRepository.save(employee);
    log.info("Berhasil menambahkan pegawai baru dengan ID: {}", employee.getEmployeeId());
  }

  @Override
  public List<Employee> getAllEmployees(String adminId) {
    if (adminId == null || adminId.isBlank()) {
      log.warn("Gagal mengambil daftar pegawai: Sesi admin tidak valid");
      throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
    }

    log.debug("Mengambil seluruh data pegawai oleh admin ID: {}", adminId);
    return employeeRepository.findAll();
  }

  @Override
  public Employee getEmployeeById(String employeeId) {
    return employeeRepository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Pencarian pegawai gagal: ID {} tidak ditemukan", employeeId);
          return new IllegalArgumentException(
              "Employee dengan ID " + employeeId + " tidak ditemukan");
        });
  }

  @Override
  public Employee getEmployeeByUsername(String username) {
    EmployeeAccount account = employeeAccountRepository
        .findByUsername(username)
        .orElseThrow(() -> {
          log.warn("Pencarian akun pegawai gagal: Username {} tidak ditemukan", username);
          return new IllegalArgumentException("Employee dengan username tidak ditemukan");
        });

    String employeeId = account.getEmployeeId();
    return employeeRepository
        .findById(employeeId)
        .orElseThrow(() -> {
          log.warn("Data employee fisik tidak ditemukan untuk ID: {}", employeeId);
          return new IllegalArgumentException(
              "Employee dengan ID " + employeeId + " tidak ditemukan");
        });
  }
}