package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.model.employee.EmployeeAccount;
import org.example.service.employee.AuthEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginEmployeeController {
    private final AuthEmployeeService authEmployeeService;

    @PostMapping("/register")
    public ResponseEntity<String> registerEmployeesAccount(@Valid @RequestBody RegisterRequest request, @RequestHeader("X-Admin-id") String adminId){
        try {
            EmployeeAccount account = new EmployeeAccount();
            account.setEmployeeId(request.getEmployeeId());
            account.setUsername(request.getUsername());
            account.setPassword(request.getPassword());
            account.setRoleId(request.getRoleId());
            authEmployeeService.registerEmployeesAccount(account, adminId);
            return ResponseEntity.ok("EmployeeAccountMessage.REGISTER_SUCCESS.getDesc()");
        } catch (IllegalArgumentException | IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login-employee")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response){
        boolean isSuccess = authEmployeeService.adminLogin(request.getUsername(), request.getPassword());

        if (isSuccess){
            String token = authEmployeeService.generateToken(request.getUsername());
            Cookie jwtCookie = new Cookie("accessToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24*60*60);

            response.addCookie(jwtCookie);
            return ResponseEntity.ok(Map.of("message", "Login sukses!"));
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Login gagal!"));
        }
    }
}
