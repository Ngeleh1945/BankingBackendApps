package org.example.model.region;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "region")
public class Region {
  @Id
  @Column(name = "regioncode")
  private String regionCode;
  @Column(name = "country")
  private String country;
  @Column(name = "state")
  private String state;
  @Column(name = "city")
  private String city;
  @Column(name = "district")
  private String district;
  @Column(name = "subdistrict")
  private String subDistrict;
}
