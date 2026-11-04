package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.region.RegionRequest;
import org.example.dto.region.RegionResponse;
import org.example.model.region.Region;
import org.example.service.region.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {
  private final RegionService service;

  @PostMapping("/add")
  public ResponseEntity<Region> addRegion(@RequestBody RegionRequest request) {
    return ResponseEntity.ok(service.addRegion(request));
  }

  @GetMapping("/{regionCode}")
  public ResponseEntity<RegionResponse> getRegionById(
      @PathVariable("regionCode") String regionCode) {

    return service
        .findRegionById(regionCode)
        .map(region -> ResponseEntity.ok(
            new RegionResponse(
                region.getRegionCode(),
                region.getCountry(),
                region.getState(),
                region.getCity(),
                region.getDistrict(),
                region.getSubDistrict()
            )
        ))
        .orElseGet(() -> ResponseEntity
            .notFound()
            .build());
  }

  @DeleteMapping("/{regionCode}")
  public ResponseEntity<Void> deleteRegion(@PathVariable String regionCode) {
    service.deleteRegion(regionCode);
    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping("/getCountries")
  public ResponseEntity<List<String>> getCountries() {
    return ResponseEntity.ok(service.getCountries());
  }

  @GetMapping("/getStates")
  public ResponseEntity<List<String>> getState(@RequestParam String country) {
    return ResponseEntity.ok(service.getStatesByCountry(country));
  }

  @GetMapping("/getCities")
  public ResponseEntity<List<String>> getCities(
      @RequestParam String country,
      @RequestParam String state
  ) {
    return ResponseEntity.ok(service.getCitiesByCountryAndState(country, state));
  }

  @GetMapping("/getDistrict")
  public ResponseEntity<List<String>> getDistrict(
      @RequestParam String country,
      @RequestParam String state,
      @RequestParam String city
  ) {
    return ResponseEntity.ok(service.getDistricts(country, state, city));
  }

  @GetMapping("/getSubdistrict")
  public ResponseEntity<List<String>> getSubdistrict(
      @RequestParam String country,
      @RequestParam String state,
      @RequestParam String city,
      @RequestParam String district
  ) {
    return ResponseEntity.ok(service.getSubdistricts(country, state, city, district));
  }

}