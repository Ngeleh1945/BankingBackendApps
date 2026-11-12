package org.example.service.employee.implementation;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginResult;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.enums.EmployeeAccountMessage;
import org.example.enums.LoginMessage;
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
  public RegisterResponse registerEmployeesAccount(RegisterRequest request) {

    if (authRepository.existsByUsername(request.getUsername())) {
      throw new IllegalStateException(EmployeeAccountMessage.USERNAME_ALREADY_EXIST.getDesc());
    }

    String rawPassword = request.getPassword();
    String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

    request.setPassword(hashedPassword);

    EmployeeAccount employeeAccount = new EmployeeAccount();
    employeeAccount.setEmployeeId(request.getEmployeeId());
    employeeAccount.setUsername(request.getUsername());
    employeeAccount.setPassword(request.getPassword());
    employeeAccount.setRoleId(request.getRoleId());
    authRepository.save(employeeAccount);
    log.info("Employee account registered successfully for username: {}",
        employeeAccount.getUsername());

    return RegisterResponse.builder()
        .employeeId(employeeAccount.getEmployeeId())
        .username(employeeAccount.getUsername())
        .password(employeeAccount.getPassword())
        .roleId(employeeAccount.getRoleId())
        .build();
  }

  @Override
  public LoginResult adminLogin(String username, String password) {

    EmployeeAccount user = authRepository
        .findByUsername(username)
        .orElseThrow(() -> new BadCredentialsException(
            LoginMessage.ACCOUNT_NOTFOUND.getDesc()
        ));

    boolean valid = BCrypt.checkpw(password, user.getPassword());

    if (!valid) {
      throw new BadCredentialsException(
          LoginMessage.WRONGPASSWORD.getDesc()
      );
    }

    String sessionId = UUID.randomUUID().toString();

    String token = jwtService.generateToken(username, sessionId);

    boolean sessionCreated = redisSessionService.createSession(username, sessionId);

    if (!sessionCreated) {
      throw new IllegalStateException(LoginMessage.SESSIONACTIVE.getDesc());
    }

    return new LoginResult(username, token);
  }

  @Override
  public void adminLogout(String username) {
    redisSessionService.deleteSession(username);
  }
}