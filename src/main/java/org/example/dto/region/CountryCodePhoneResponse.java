package org.example.dto.region;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CountryCodePhoneResponse {
  private String isoCode;
  private String country;
  private String countryTelephoneCodes;
}
