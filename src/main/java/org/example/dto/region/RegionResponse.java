package org.example.dto.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegionResponse {
  private String regionCode;
  private String country;
  private String state;
  private String city;
  private String district;
  private String subdistrict;
}
