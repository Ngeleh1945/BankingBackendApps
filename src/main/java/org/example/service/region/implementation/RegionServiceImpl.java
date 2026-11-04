package org.example.service.region.implementation;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.region.RegionRequest;
import org.example.model.region.Region;
import org.example.repository.region.RegionRepository;
import org.example.service.region.RegionService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing geographic and administrative region operations.
 * Handles business logic related to hierarchical region lookups and data manipulation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
  private final RegionRepository repository;

  @Override
  public Region addRegion(RegionRequest request) {
    Region region = new Region();
    region.setRegionCode(request.getRegionCode());
    region.setCountry(request.getCountry());
    region.setState(request.getState());
    region.setCity(request.getCity());
    region.setDistrict(request.getDistrict());
    region.setSubDistrict(request.getSubDistrict());

    return repository.save(region);
  }

  @Override
  public Optional<Region> findRegionById(String regionCode) {
    return repository.findById(regionCode);
  }

  @Override
  public void deleteRegion(String regionCode) {
    Region region = repository.findById(regionCode)
        .orElseThrow(() ->
            new EntityNotFoundException(
                "Region tidak ditemukan: " + regionCode
            )
        );

    repository.delete(region);
  }

  @Override
  public List<String> getCountries() {
    return repository.findCountries();
  }

  @Override
  public List<String> getStatesByCountry(String country) {
    return repository.findStatesByCountry(country);
  }

  @Override
  public List<String> getCitiesByCountryAndState(String country, String state) {
    return repository.findCitiesByCountryAndState(country, state);
  }

  @Override
  public List<String> getDistricts(String country, String state, String city) {
    return repository.findDistrictsByCountryStateAndCity(country, state, city);
  }

  @Override
  public List<String> getSubdistricts(String country, String state, String city, String district) {
    return repository.findSubdistricts(country, state, city, district);
  }
}
