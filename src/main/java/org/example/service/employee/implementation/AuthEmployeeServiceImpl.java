package org.example.service.employee.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginResult;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.dto.auth.ShowAccountEmployeeResponse;
import org.example.enums.EmployeeAccountMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.RoleAccountRepository;
import org.example.service.employee.AuthEmployeeService;
import org.example.service.jwt.JwtService;
import org.example.service.session.RedisSessionService;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee authentication and account operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEmployeeServiceImpl implements AuthEmployeeService {
  private final EmployeeAccountRepository authRepository;
  private final RoleAccountRepository roleAccountRepository;
  private final JwtService jwtService;
  private final RedisSessionService redisSessionService;

  @Value("${app.security.cookie.secure:false}")
  private boolean isCookieSecure;

  @Override
  @Transactional
  public RegisterResponse registerEmployeesAccount(@NotNull RegisterRequest request) {

    if (authRepository.existsById(request.getEmployeeId())) {
      throw new IllegalStateException(
          EmployeeAccountMessage.EMPLOYEE_ALREADY_HAS_ACCOUNT.format(request.getEmployeeId()
          ));
    }

    if (authRepository.existsByUsername(request.getUsername())) {
      throw new IllegalStateException(
          EmployeeAccountMessage.USERNAME_ALREADY_EXISTS.format(request.getUsername()
          ));
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

    return RegisterResponse.builder()
        .employeeId(employeeAccount.getEmployeeId())
        .username(employeeAccount.getUsername())
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
  public List<ShowAccountEmployeeResponse> showAccounts() {
    return authRepository
        .findEmployeeAccounts();
  }

  @Override
  public EmployeeAccount getAccountById(String employeeId) {
    return authRepository.findById(employeeId)
        .orElseThrow(() ->
            new IllegalArgumentException(
                LoginMessage.ACCOUNTWITHEMPLOYEEID_NOT_FOUND.format(employeeId)
            )
        );
  }

  @Override
  @Transactional
  public EmployeeAccount updateRole(
      String employeeId,
      @NotNull EmployeeAccount request,
      @NotNull Authentication authentication
  ) {
    EmployeeAccount account = authRepository.findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            LoginMessage.ACCOUNTWITHEMPLOYEEID_NOT_FOUND.format(employeeId)
        ));

    validateRolePermission(authentication);
    validateAccountNotInUse(account, "modify");

    account.setRoleId(request.getRoleId());

    return authRepository.save(account);
  }

  @Override
  @Transactional
  public void deleteAccount(String employeeId, Authentication authentication) {
    EmployeeAccount account = authRepository.findById(employeeId)
        .orElseThrow(() -> new EntityNotFoundException(
            LoginMessage.ACCOUNTWITHEMPLOYEEID_NOT_FOUND.format(employeeId)
        ));

    validateRolePermission(authentication);
    validateAccountNotInUse(account, "delete");

    authRepository.delete(account);
  }

  @Override
  public void adminLogout(String username) {
    redisSessionService.deleteSession(username);
  }

  private void validateRolePermission(@NotNull Authentication authentication) {
    boolean hasPermission = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority ->
            grantedAuthority.getAuthority().equals("Admin")
            || grantedAuthority.getAuthority().equals("Super Admin")
        );

    if (!hasPermission) {
      throw new AccessDeniedException("You are not authorized to update account role");
    }
  }

  private void validateAccountNotInUse(EmployeeAccount account, String action) {
    if (redisSessionService.hasActiveSession(account.getUsername())) {
      throw new IllegalStateException(
          "Cannot " + action + "account because it's currently in use"
      );
    }
  }
}