package org.example.service.employee;

import org.example.model.employee.EmployeeAccount;

public interface AuthEmployeeService {
    boolean registerEmployeesAccount(EmployeeAccount account, String adminId);
    boolean adminLogin(String username, String password);
    boolean isCurrentLoggedIn();
    EmployeeAccount getCurrentLoggedIn();
    String generateToken(String username);
    String extractUsernameFromToken(String token);
    EmployeeAccount getAccountByUsername(String username);
}
