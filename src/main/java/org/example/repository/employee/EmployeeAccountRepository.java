package org.example.repository.employee;

import org.example.model.employee.EmployeeAccount;

import java.util.List;
import java.util.Optional;

public interface EmployeeAccountRepository {
    void save(EmployeeAccount employeeId);
    Optional<EmployeeAccount> findByUsername(String Username);
    List<EmployeeAccount> findAll();
    void delete(String employee);
}
