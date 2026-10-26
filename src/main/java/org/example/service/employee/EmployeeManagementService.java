package org.example.service.employee;

import org.example.model.employee.Employee;
import org.example.repository.employee.EmployeeRepository;

import java.util.List;

public interface EmployeeManagementService {
    boolean addEmployees(Employee employee, String adminId);
    List<Employee> getAllEmployees(String adminId);
    Employee getEmployeeById(String employeeId);
    Employee getEmployeeByUsername(String username);
}
