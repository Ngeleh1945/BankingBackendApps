package org.example.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CountryCodePhoneResponse {
  private String isoCode;
  private String country;
  private String countryTelephoneCodes;
}
