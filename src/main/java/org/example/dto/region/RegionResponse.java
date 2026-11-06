package org.example.dto.region;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponse {
  private String regionCode;
  private String country;
  private String state;
  private String city;
  private String district;
  private String subdistrict;
}
