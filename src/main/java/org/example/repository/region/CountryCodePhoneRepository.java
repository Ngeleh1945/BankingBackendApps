package org.example.repository.region;

import java.util.List;
import org.example.dto.region.CountryCodePhoneResponse;
import org.example.model.region.CountryCodePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing country telephone codes and ISO identifiers.
 * Provides data access methods to retrieve standardized country codes and phone extensions.
 */
public interface CountryCodePhoneRepository extends JpaRepository<CountryCodePhone, String> {

  /**
   * Retrieves a distinct list of all country ISO codes and their corresponding telephone dialing
   * codes, ordered alphabetically by the country name.
   *
   * @return a {@link List} of {@link CountryCodePhoneResponse} containing ISO codes and phone codes
   */
  @Query(
      """
          SELECT NEW org.example.dto.region.CountryCodePhoneResponse(ccp.isoCode, ccp.country, ccp.countryTelephoneCodes)
          FROM CountryCodePhone ccp
          ORDER BY ccp.country
      """)
  List<CountryCodePhoneResponse> findCountryCodes();
}