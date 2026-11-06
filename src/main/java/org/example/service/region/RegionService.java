package org.example.service.region;

import java.util.List;
import java.util.Optional;
import org.example.dto.region.RegionRequest;
import org.example.dto.region.RegionResponse;
import org.example.model.region.Region;

/**
 * Service interface for managing geographic and administrative region operations.
 * Handles business logic related to hierarchical region lookups and data manipulation.
 */
public interface RegionService {

  /**
   * Adds or saves a new region entity into the system.
   *
   * @param region the {@link Region} object to be added
   * @return the saved {@link Region} instance
   */
  public RegionResponse addRegion(RegionRequest region);

  /**
   * Finds a specific region by its unique region code identifier.
   *
   * @param regionCode the unique code of the region
   * @return an {@link Optional} containing the found region, or empty if not found
   */
  Optional<Region> findRegionById(String regionCode);

  /**
   * Deletes a region record based on its unique region code.
   *
   * @param regionCode the unique code of the region to be deleted
   */
  void deleteRegion(String regionCode);

  /**
   * Retrieves a list of all unique available countries.
   *
   * @return a {@link List} of country names as strings
   */
  List<String> getCountries();

  /**
   * Retrieves a list of all states associated with a specific country.
   *
   * @param country the name of the country
   * @return a {@link List} of state names as strings
   */
  List<String> getStatesByCountry(String country);

  /**
   * Retrieves a list of cities located within a specific country and state.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @return a {@link List} of city names as strings
   */
  List<String> getCitiesByCountryAndState(String country, String state);

  /**
   * Retrieves a list of districts matching the specified country, state, and city.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @param city    the name of the city or regency
   * @return a {@link List} of district names as strings
   */
  List<String> getDistricts(String country, String state, String city);

  /**
   * Retrieves a list of sub-districts matching the complete hierarchical path
   * of country, state, city, and district.
   *
   * @param country  the name of the country
   * @param state    the name of the state or province
   * @param city     the name of the city or regency
   * @param district the name of the district
   * @return a {@link List} of sub-district or village names as strings
   */
  List<String> getSubdistricts(String country, String state, String city, String district);

  /**
   * Retrieves a list of all state names available in the system.
   *
   * @return a {@link List} of state names as strings
   */
  List<String> getAllCity();
}