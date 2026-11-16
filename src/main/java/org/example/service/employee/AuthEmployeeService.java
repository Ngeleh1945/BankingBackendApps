package org.example.service.employee;

import java.util.List;
import java.util.Optional;
import org.example.dto.auth.LoginResult;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.dto.auth.ShowAccountEmployeeResponse;
import org.example.model.employee.EmployeeAccount;
import org.springframework.security.core.Authentication;

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
   * Retrieves a consolidated list or details of all employee accounts within the system.
   *
   * @return the {@link ShowAccountEmployeeResponse} containing employee account details
   */
  List<ShowAccountEmployeeResponse> showAccounts();

  EmployeeAccount getAccountById(String employeeId);

  EmployeeAccount updateRole(String employeeId, EmployeeAccount request, Authentication authentication);

  /**
   * Performs the administrative logout process for a specific user.
   * Invalidates active sessions or tokens associated with the given username.
   *
   * @param username the unique username of the admin logging out
   */
  void adminLogout(String username);
}