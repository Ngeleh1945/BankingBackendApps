package org.example.service.employee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.enums.EmployeeAccountMessage;
import org.example.enums.LoginMessage;
import org.example.model.employee.Employee;
import org.example.model.employee.EmployeeAccount;
import org.example.repository.employee.EmployeeAccountRepository;
import org.example.repository.employee.EmployeeRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthEmployeeServiceImpl implements AuthEmployeeService {
    private final EmployeeAccountRepository authRepository;
    private final EmployeeRepository employeeRepository;
    private EmployeeAccount currentLoggedIn;
    @Value("${jwt.secret}")
    private String secretKey;
    private final long expirationTime = 24*60*60*1000;
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public boolean registerEmployeesAccount(EmployeeAccount account, String adminId){
        if (adminId == null || adminId.isBlank()){
            throw new IllegalStateException(LoginMessage.INVALIDSESSION.getDesc());
        }

        Optional<EmployeeAccount> existingAccount = authRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()){
            throw new IllegalStateException(EmployeeAccountMessage.USERNAME_ALREADY_EXIST.getDesc());
        }

        String rawPassword = account.getPassword();
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        account.setPassword(hashedPassword);
        authRepository.save(account);
        return true;
    }

    @Override
    public boolean adminLogin(String username, String password){
        if (username == null || password == null || username.isBlank() || password.isBlank()){
            System.out.println(LoginMessage.USERNAMEANDPASSWORD_ISNULL.getDesc());
            return false;
        }

        EmployeeAccount user = authRepository.findByUsername(username).orElse(null);
        boolean cocok = BCrypt.checkpw(password, user.getPassword());

        System.out.println("Input password : " + password);
        System.out.println("Hash database  : " + user.getPassword());
        System.out.println("Password cocok : " + cocok);

        if (!cocok){
            System.out.println(LoginMessage.WRONGPASSWORD.getDesc());
            return false;
        }

        if (user == null){
            System.out.println(LoginMessage.ACCOUNT_NOTFOUND.getDesc());
            return false;
        }

        if (!BCrypt.checkpw(password, user.getPassword())){
            System.out.println(LoginMessage.WRONGPASSWORD.getDesc());
            return false;
        }

        this.currentLoggedIn = user;
        System.out.println(LoginMessage.LOGINSUCCESS.getDesc() + searchName(user.getEmployeeId()));
        return true;
    }

    public boolean isCurrentLoggedIn(){
        return this.currentLoggedIn != null;
    }

    @Override
    public String generateToken(String username){
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsernameFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    @Override
    public EmployeeAccount getAccountByUsername(String username){
        return authRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("Account dengan username " + username + " tidak ditemukan"));
    }
    public EmployeeAccount getCurrentLoggedIn(){
        return this.currentLoggedIn;
    }

    private String searchName(String employeeId){
        return employeeRepository.findById(employeeId).map(Employee::getFullName).orElse("STAFF ID : " + employeeId);
    }
}
