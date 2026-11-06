package org.example.service.region;

import java.util.List;
import java.util.Optional;
import org.example.dto.region.CountryCodePhoneRequest;
import org.example.dto.region.CountryCodePhoneResponse;

/**
 * Service interface for managing country telephone dialing codes and ISO configurations.
 */
public interface CountryCodePhoneService {

  /**
   * Adds a new country phone code mapping into the system.
   *
   * @param request the {@link CountryCodePhoneRequest} containing country code details to add
   * @return a {@link CountryCodePhoneResponse} representing the newly created country code entry
   */
  CountryCodePhoneResponse addCountryCodePhone(CountryCodePhoneRequest request);

  /**
   * Retrieves a specific country phone code configuration by its ISO code.
   *
   * @param isoCode the unique ISO country code to search for
   * @return an {@link Optional} containing the found {@link CountryCodePhoneResponse},
   *      or empty if not found
   */
  Optional<CountryCodePhoneResponse> findCountryCodeByIso(String isoCode);

  /**
   * Deletes a country phone code configuration from the system based on its ISO code.
   *
   * @param isoCode the unique ISO country code of the entry to delete
   */
  void deleteCountryPhone(String isoCode);

  /**
   * Retrieves a list of all available country phone code configurations.
   *
   * @return a {@link List} of {@link CountryCodePhoneResponse} containing all entries
   */
  List<CountryCodePhoneResponse> findCountryCodes();
}