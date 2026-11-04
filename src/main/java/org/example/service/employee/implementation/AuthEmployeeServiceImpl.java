package org.example.service.employee.implementation;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginResult;
import org.example.enums.EmployeeAccountMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.AuthEmployeeService;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee authentication and account operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEmployeeServiceImpl implements AuthEmployeeService {
  private final EmployeeAccountRepository authRepository;
  private final EmployeeRepository employeeRepository;
  private final JwtService jwtService;
  private final RedisSessionService redisSessionService;

  @Value("${app.security.cookie.secure:false}")
  private boolean isCookieSecure;

  @Override
  public boolean registerEmployeesAccount(EmployeeAccount account, String adminId) {
    if (adminId == null || adminId.isBlank()) {
      throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
    }

    Optional<EmployeeAccount> existingAccount =
        authRepository.findByUsername(account.getUsername());
    if (existingAccount.isPresent()) {
      throw new IllegalStateException(EmployeeAccountMessage.USERNAME_ALREADY_EXIST.getDesc());
    }

    String rawPassword = account.getPassword();
    String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

    account.setPassword(hashedPassword);
    authRepository.save(account);
    log.info("Employee account registered successfully for username: {}", account.getUsername());
    return true;
  }

  @Override
  public LoginResult adminLogin(String username, String password) {
    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      log.warn(LoginMessage.USERNAMEANDPASSWORD_ISNULL.getDesc());
      throw new BadCredentialsException(LoginMessage.USERNAMEANDPASSWORD_ISNULL.getDesc());
    }

    EmployeeAccount user = authRepository
        .findByUsername(username)
        .orElseThrow(() -> new BadCredentialsException(
            LoginMessage.USERPASSWRONG.getDesc()
        ));

    boolean cocok = BCrypt.checkpw(password, user.getPassword());

    log.info("Password cocok : {}", cocok);

    if (!cocok) {
      log.error(LoginMessage.WRONGPASSWORD.getDesc());
      throw new BadCredentialsException(LoginMessage.WRONGPASSWORD.getDesc());
    }

    String token = jwtService.generateToken(username);

    boolean sessionCreated = redisSessionService.createSession(username, token);

    if (!sessionCreated) {
      log.warn(LoginMessage.SESSIONACTIVE.getDesc());

      throw new IllegalStateException(LoginMessage.SESSIONACTIVE.getDesc());
    }

    log.info("{}{}", LoginMessage.LOGINSUCCESS.getDesc(),
        searchName(user.getEmployeeId()));

    return new LoginResult(username, token);
  }

  @Override
  public void adminLogout(String username) {
    redisSessionService.deleteSession(username);
  }

  @Override
  public EmployeeAccount getAccountByUsername(String username) {
    return authRepository
        .findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException(
            "Account dengan username " + username + " tidak ditemukan"));
  }

  private String searchName(String employeeId) {
    return employeeRepository
        .findById(employeeId)
        .map(Employee::getFullName)
        .orElse("STAFF ID : " + employeeId);
  }
}