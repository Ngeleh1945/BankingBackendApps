package org.example.model.region;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "countrycodesphone")
public class CountryCodePhone {
  @Id
  @Column(name = "isocode")
  private String isoCode;
  @Column(name = "country")
  private String country;
  @Column(name = "countrytelephonecodes")
  private String countryTelephoneCodes;
}
