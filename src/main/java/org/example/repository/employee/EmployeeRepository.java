package org.example.repository.employee;

import java.util.Optional;
import org.example.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository Employee for managing employee data.
 */
public interface EmployeeRepository extends JpaRepository<Employee, String> {
  /**
   * Finds an employee entity by their unique National Identity Number (NIK).
   *
   * @param nik the National Identity Number to search for
   * @return an {@link Optional} containing the found {@link Employee}, or empty if not found
   */
  @Query(
      """
       SELECT e
       FROM Employee e
       WHERE e.nik = :nik
      """)
  Optional<Employee> findByNik(@Param("nik") String nik);
}