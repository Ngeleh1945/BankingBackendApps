package org.example.service.employee;

import org.example.dto.auth.LoginResult;
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
  LoginResult adminLogin(String username, String password);

  /**
   * Logout
   * @param username
   */
  void adminLogout(String username);

  /**
   * Retrieves an employee account by its username.
   *
   * @param username the username to search for
   * @return the EmployeeAccount instance if found
   */
  EmployeeAccount getAccountByUsername(String username);
}