package org.example.service.employee;

import org.example.model.employee.EmployeeAccount;

/**
 * Service interface for managing employee authentication and account operations.
 */
public interface AuthEmployeeService {
  /**
   * Registers a new employee account.
   *
   * @param account the employee account details to register
   * @param adminId the identifier of the admin performing the registration
   * @return true if registration is successful
   */
  boolean registerEmployeesAccount(EmployeeAccount account, String adminId);

  /**
   * Authenticates an employee login attempt.
   *
   * @param username the username of the employee
   * @param password the raw password of the employee
   * @return true if authentication is successful
   */
  boolean adminLogin(String username, String password);

  /**
   * Generates a JWT token for the authenticated employee.
   *
   * @param username the username for which the token is generated
   * @return the generated JWT token string
   */
  String generateToken(String username);

  /**
   * Extracts the username from a given JWT token.
   *
   * @param token the JWT token string
   * @return the username extracted from the token claims
   */
  String extractUsernameFromToken(String token);

  /**
   * Retrieves an employee account by its username.
   *
   * @param username the username to search for
   * @return the EmployeeAccount instance if found
   */
  EmployeeAccount getAccountByUsername(String username);
}