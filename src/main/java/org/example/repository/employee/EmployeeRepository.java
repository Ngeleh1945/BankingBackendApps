package org.example.repository.employee;

import org.example.model.employee.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    void save(Employee employee);
    Optional<Employee> findById(String employeeId);
    List<Employee> findAll();
    void delete(String employeeId);
}
