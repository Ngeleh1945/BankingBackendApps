package org.example.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountryCodePhoneRequest {
  @NotBlank(message = "ISO code cannot be blank")
  private String isoCode;
  @NotBlank(message = "country cannot be blank")
  private String country;
  @NotBlank(message = "Country telephone code cannot be blank")
  private String countryTelephoneCodes;
}
