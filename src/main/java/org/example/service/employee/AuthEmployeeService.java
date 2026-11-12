package org.example.service.employee;

import org.example.dto.auth.LoginResult;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.model.employee.EmployeeAccount;

/**
 * Service interface for managing employee authentication and account operations.
 */
public interface AuthEmployeeService {
  /**
   * Registers a new employee account.
   *
   * @param account the employee account details to register
   */
  RegisterResponse registerEmployeesAccount(RegisterRequest account);

  /**
   * Authenticates an employee login attempt.
   *
   * @param username the username of the employee
   * @param password the raw password of the employee
   */
  LoginResult adminLogin(String username, String password);

  /**
   * Performs the administrative logout process for a specific user.
   * Invalidates active sessions or tokens associated with the given username.
   *
   * @param username the unique username of the admin logging out
   */
  void adminLogout(String username);
}