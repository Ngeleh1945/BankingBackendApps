package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.managementbank.ShowBankCode;
import org.example.service.managementbank.BankCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for managing bank code configurations and retrieving bank details.
 */
@RestController
@RequestMapping("/api/bank-code")
@RequiredArgsConstructor
public class BankCodeController {
  private final BankCodeService service;

  /**
   * Retrieves a list of all available bank code configurations.
   *
   * @return a {@link ResponseEntity}
   * containing a list of {@link ShowBankCode} objects with HTTP 200 status
   */
  @GetMapping("/getAllBankName")
  public ResponseEntity<List<ShowBankCode>> getAllBankName() {
    return ResponseEntity.ok(service.findAllBank());
  }
}
