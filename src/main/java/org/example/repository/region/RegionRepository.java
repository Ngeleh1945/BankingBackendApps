package org.example.repository.region;

import java.util.List;
import org.example.model.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing hierarchical geographic and administrative regions.
 * Provides data access methods to query countries, states, cities, districts,
 * and sub-districts structured in a multi-level format.
 */
public interface RegionRepository extends JpaRepository<Region, String> {

  /**
   * Retrieves a distinct list of all available countries within the region dataset.
   *
   * @return a {@link List} of country names as strings
   */
  @Query(
      """
      SELECT DISTINCT r.country
      FROM Region r
      ORDER BY r.country
      """)
  List<String> findCountries();

  /**
   * Retrieves all state-level regions associated with a specified country.
   *
   * @param country the name of the country to filter by
   * @return a {@link List} of state names as strings
   */
  @Query(
      """
      SELECT DISTINCT r.state
      FROM Region r
      WHERE r.country = :country
      ORDER BY r.state
      """)
  List<String> findStatesByCountry(@Param("country") String country);

  /**
   * Retrieves all city-level regions located within a specific country and state.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @return a {@link List} of city names as strings
   */
  @Query(
      """
      SELECT DISTINCT r.city
      FROM Region r
      WHERE r.country = :country AND r.state = :state
      ORDER BY r.city
      """)
  List<String> findCitiesByCountryAndState(
      @Param("country") String country,
      @Param("state") String state);

  /**
   * Retrieves all district-level regions matching the specified country, state, and city.
   *
   * @param country the name of the country
   * @param state   the name of the state or province
   * @param city    the name of the city or regency
   * @return a {@link List} of district names as strings
   */
  @Query(
      """
      SELECT DISTINCT r.district
      FROM Region r
      WHERE r.country = :country AND r.state = :state AND r.city = :city
      ORDER BY r.district
      """)
  List<String> findDistrictsByCountryStateAndCity(
      @Param("country") String country,
      @Param("state") String state,
      @Param("city") String city);

  /**
   * Retrieves all sub-district or village-level regions matching the complete hierarchical path.
   *
   * @param country  the name of the country
   * @param state    the name of the state or province
   * @param city     the name of the city or regency
   * @param district the name of the district
   * @return a {@link List} of sub-district or village names as strings
   */
  @Query(
      """
      SELECT DISTINCT r.subDistrict
      FROM Region r
      WHERE r.country = :country 
        AND r.state = :state 
        AND r.city = :city 
        AND r.district = :district
      ORDER BY r.subDistrict
      """)
  List<String> findSubdistricts(
      @Param("country") String country,
      @Param("state") String state,
      @Param("city") String city,
      @Param("district") String district);
}