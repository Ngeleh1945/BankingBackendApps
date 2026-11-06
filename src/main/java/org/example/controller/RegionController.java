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

/**
 * REST controller for managing geographical region data, including countries,
 * states/provinces, cities, districts, and subdistricts.
 *
 * @author YourName
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {
  private final RegionService service;

  /**
   * Adds a new region entry to the database.
   *
   * @param request the region payload containing location details
   * @return a {@link ResponseEntity} containing the saved
   *      {@link Region} object and HTTP status 200 (OK)
   */
  @PostMapping("/add")
  public ResponseEntity<RegionResponse> addRegion(@RequestBody RegionRequest request) {
    return ResponseEntity.ok(service.addRegion(request));
  }

  /**
   * Retrieves specific region details by its unique region code.
   *
   * @param regionCode the unique code identifying the region
   * @return a {@link ResponseEntity} containing {@link RegionResponse} if found (HTTP 200),
   *         or HTTP 404 (Not Found) if no match exists
   */
  @GetMapping("/{regionCode}")
  public ResponseEntity<RegionResponse> getRegionById(
      @PathVariable("regionCode") String regionCode) {

    return service
        .findRegionById(regionCode)
        .map(region -> ResponseEntity.ok(
            RegionResponse.builder()
                .regionCode(region.getRegionCode())
                .country(region.getCountry())
                .state(region.getState())
                .city(region.getCity())
                .district(region.getDistrict())
                .subdistrict(region.getSubDistrict())
                .build()
        ))
        .orElseGet(() -> ResponseEntity
            .notFound()
            .build());
  }

  /**
   * Deletes a region record by its unique region code.
   *
   * @param regionCode the unique code of the region to be deleted
   * @return a {@link ResponseEntity} with HTTP status 204 (No Content) upon successful deletion
   */
  @DeleteMapping("/{regionCode}")
  public ResponseEntity<Void> deleteRegion(@PathVariable String regionCode) {
    service.deleteRegion(regionCode);
    return ResponseEntity
        .noContent()
        .build();
  }

  /**
   * Retrieves a list of all available countries.
   *
   * @return a {@link ResponseEntity} containing a list of country names with HTTP status 200 (OK)
   */
  @GetMapping("/getCountries")
  public ResponseEntity<List<String>> getCountries() {
    return ResponseEntity.ok(service.getCountries());
  }

  /**
   * Retrieves a list of states/provinces for a specific country.
   *
   * @param country the name of the target country
   * @return a {@link ResponseEntity} containing a list of state/province
   *      names with HTTP status 200 (OK)
   */
  @GetMapping("/getStates")
  public ResponseEntity<List<String>> getState(@RequestParam String country) {
    return ResponseEntity.ok(service.getStatesByCountry(country));
  }

  /**
   * Retrieves a list of cities based on the provided country and state/province.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @return a {@link ResponseEntity} containing a list of city names with HTTP status 200 (OK)
   */
  @GetMapping("/getCities")
  public ResponseEntity<List<String>> getCities(
      @RequestParam String country,
      @RequestParam String state
  ) {
    return ResponseEntity.ok(service.getCitiesByCountryAndState(country, state));
  }

  /**
   * Retrieves a list of districts based on the specified country, state, and city.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @param city    the name of the city
   * @return a {@link ResponseEntity} containing a list of district names with HTTP status 200 (OK)
   */
  @GetMapping("/getDistrict")
  public ResponseEntity<List<String>> getDistrict(
      @RequestParam String country,
      @RequestParam String state,
      @RequestParam String city
  ) {
    return ResponseEntity.ok(service.getDistricts(country, state, city));
  }

  /**
   * Retrieves a list of subdistricts based on the full region hierarchy
   * (country, state, city, and district).
   *
   * @param country  the name of the country
   * @param state    the name of the state or province
   * @param city     the name of the city
   * @param district the name of the district
   * @return a {@link ResponseEntity} containing a list of subdistrict names
   *      with HTTP status 200 (OK)
   */
  @GetMapping("/getSubdistrict")
  public ResponseEntity<List<String>> getSubdistrict(
      @RequestParam String country,
      @RequestParam String state,
      @RequestParam String city,
      @RequestParam String district
  ) {
    return ResponseEntity.ok(service.getSubdistricts(country, state, city, district));
  }

  @GetMapping("/getAllCity")
  public ResponseEntity<List<String>> getAllCity() {
    return ResponseEntity.ok(service.getAllCity());
  }
}