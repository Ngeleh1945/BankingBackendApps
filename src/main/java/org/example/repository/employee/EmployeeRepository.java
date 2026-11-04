package org.example.repository.employee;

import org.example.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Employee for managing employee data.
 */
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}