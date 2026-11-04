package org.example.service.region;

import java.util.List;
import java.util.Optional;
import org.example.dto.region.CountryCodePhoneRequest;
import org.example.dto.region.CountryCodePhoneResponse;
import org.example.model.region.CountryCodePhone;

public interface CountryCodePhoneService {
  public CountryCodePhoneResponse addCountryCodePhone(CountryCodePhoneRequest request);
  Optional<CountryCodePhoneResponse> findCountryCodeByIso(String isoCode);
  void deleteCountryPhone(String isoCode);
  List<CountryCodePhoneResponse> findCountryCodes();

}
