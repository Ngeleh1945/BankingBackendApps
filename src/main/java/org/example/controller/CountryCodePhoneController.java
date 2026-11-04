package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.region.CountryCodePhoneResponse;
import org.example.service.region.CountryCodePhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/country-code-phone")
@RequiredArgsConstructor
public class CountryCodePhoneController {
  private final CountryCodePhoneService service;

  @GetMapping("/getCountryCodePhone")
  public ResponseEntity<List<CountryCodePhoneResponse>> findCountryCodes() {
    return ResponseEntity.ok(service.findCountryCodes());
  }
}
