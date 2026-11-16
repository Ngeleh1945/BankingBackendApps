package org.example.repository.employee;

import java.util.List;
import java.util.Optional;
import org.example.dto.auth.ShowAccountEmployeeResponse;
import org.example.model.employee.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository Interface Employee Account managing Authentication Administration Employees.
 */
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, String> {

  /**
   * Finds an employee account by its username.
   *
   * @param username the username to search for
   * @return an Optional containing the EmployeeAccount if found, or empty otherwise
   */
  Optional<EmployeeAccount> findByUsername(String username);

  /**
   * Retrieves a list of all employee profiles and their account details.
   *
   * @return a list of {@link ShowAccountEmployeeResponse} containing account profiles
   */
  @Query(
      """
           SELECT new org.example.dto.auth.ShowAccountEmployeeResponse(
                  e.employeeId,
                  e.fullName,
                  a.username,
                  e.position,
                  r.description
              )
              FROM Employee e
              INNER JOIN EmployeeAccount a
                  ON e.employeeId = a.employeeId
              INNER JOIN RoleAccount r
                  ON a.roleId = r.roleId
      """)
  List<ShowAccountEmployeeResponse> findEmployeeAccounts();

  /**
   * Checks whether an employee account already exists based on the provided username.
   *
   * @param username the unique username to check
   * @return true if an account with the given username exists, false otherwise
   */
  boolean existsByUsername(String username);
}
