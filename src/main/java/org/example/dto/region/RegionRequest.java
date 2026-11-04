package org.example.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *  Data Transfer Object (DTO) representing a request payload for region-related operations.
 *  Used for transferring region hierarchical data (country, state, city, district, sub-district)
 *  between the client and server layers.
 */
@Getter
@Setter
@Builder
public class RegionRequest {
  private String regionCode;
  @NotBlank(message = "Country cannot be blank")
  private String country;
  @NotBlank(message = "State cannot be blank")
  private String state;
  @NotBlank(message = "City cannot be blank")
  private String city;
  @NotBlank(message = "District cannot be blank")
  private String district;
  @NotBlank(message = "Subdistrict cannot be blank")
  private String subDistrict;
}
