package org.example.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.enums.EmployeeMessage;
import org.example.model.employee.Employee;
import org.example.service.employee.EmployeeManagementService;
import org.jboss.weld.security.spi.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeManagementService employeeManagementService;

    @PostMapping
    public ResponseEntity<String> addEmployee( @RequestBody Employee employee, @RequestHeader("X-Admin-id") String adminId){
        try {
            employeeManagementService.addEmployees(employee, adminId);
            return ResponseEntity.ok(EmployeeMessage.ADD_SUCCESS.getDesc());
        } catch (IllegalArgumentException | IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees(@RequestHeader("X-Admin-Id") String adminId){
        try {
            List<Employee> employees = employeeManagementService.getAllEmployees(adminId);
            return ResponseEntity.ok(employees);
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{getEmployeeId}")
    public  ResponseEntity<?> getEmployeeId(@PathVariable String employeeId, @RequestHeader("X-Admin-Id") String adminId){
        try {
            Employee employee = employeeManagementService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request){
        try {
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

            Employee employee = employeeManagementService.getEmployeeByUsername(currentUsername);

            return ResponseEntity.ok(employee);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session habis: " + e.getMessage());
        }
    }
}
