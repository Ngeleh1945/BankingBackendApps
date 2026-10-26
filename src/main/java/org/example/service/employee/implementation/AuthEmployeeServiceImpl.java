package org.example.service.employee.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EmployeeAccountMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.example.service.employee.AuthEmployeeService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${jwt.secret}")
  private String secretKey;

  private static final long EXPIRATION_TIME = 24L * 60 * 60;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

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
  public boolean adminLogin(String username, String password) {
    if (username == null || password == null || username.isBlank() || password.isBlank()) {
      log.warn(LoginMessage.USERNAMEANDPASSWORD_ISNULL.getDesc());
      return false;
    }

    EmployeeAccount user = authRepository
        .findByUsername(username)
        .orElse(null);

    if (user == null) {
      log.warn(LoginMessage.ACCOUNT_NOTFOUND.getDesc());
      return false;
    }

    boolean cocok = BCrypt.checkpw(password, user.getPassword());

    log.debug("Input password : {}", password);
    log.debug("Hash database  : {}", user.getPassword());
    log.info("Password cocok : {}", cocok);

    if (!cocok) {
      log.warn(LoginMessage.WRONGPASSWORD.getDesc());
      return false;
    }

    log.info("{}{}", LoginMessage.LOGINSUCCESS.getDesc(), searchName(user.getEmployeeId()));
    return true;
  }

  @Override
  public String generateToken(String employeeId) {
    Instant now = Instant.now();

    long issuedAt = now.getEpochSecond();
    long expiration = now.plusSeconds(EXPIRATION_TIME).getEpochSecond();

    return Jwts.builder()
        .subject(employeeId)
        .claim("iat", issuedAt)
        .claim("exp", expiration)
        .signWith(getSigningKey())
        .compact();
  }

  @Override
  public String extractUsernameFromToken(String token) {
    Claims claims = Jwts
        .parser()
        .verifyWith((SecretKey) getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
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