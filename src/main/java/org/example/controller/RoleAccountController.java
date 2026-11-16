package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.common.ApiResponse;
import org.example.enums.RoleMessage;
import org.example.model.employee.EmployeeAccount;
import org.example.model.employee.RoleAccount;
import org.example.service.employee.RoleAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing role accounts.
 */
@RestController
@RequestMapping("/api/role-account")
@RequiredArgsConstructor
public class RoleAccountController {
  private final RoleAccountService service;

  /**
   * Registers a new role account into the system.
   *
   * @param desc the description of the role to be added
   * @return a {@link ResponseEntity} containing the standard {@link ApiResponse}
   *         with the created {@link RoleAccount}
   */
  @PostMapping("/add")
  public ResponseEntity<ApiResponse<RoleAccount>> addRole(@RequestParam String desc) {
    RoleAccount response = service.addRole(desc);

    ApiResponse<RoleAccount> apiResponse = ApiResponse.<RoleAccount>builder()
        .success(true)
        .message(RoleMessage.ADD_SUCCESSFULL.getDesc())
        .data(response)
        .build();

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(apiResponse);
  }

  @GetMapping("/show-role")
  public ResponseEntity<ApiResponse<List<RoleAccount>>> showAllRole() {
    List<RoleAccount> response = service.getAllRole();

    ApiResponse<List<RoleAccount>> apiResponse = ApiResponse.<List<RoleAccount>>builder()
        .success(true)
        .message(RoleMessage.RETRIEVE_SUCCESS.getDesc())
        .data(response)
        .build();

    return ResponseEntity.ok(apiResponse);
  }
}
