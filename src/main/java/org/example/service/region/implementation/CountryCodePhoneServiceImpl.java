package org.example.service.region.implementation;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.region.CountryCodePhoneRequest;
import org.example.dto.region.CountryCodePhoneResponse;
import org.example.model.region.CountryCodePhone;
import org.example.repository.region.CountryCodePhoneRepository;
import org.example.service.region.CountryCodePhoneService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryCodePhoneServiceImpl implements CountryCodePhoneService {
  private final CountryCodePhoneRepository repository;

  @Override
  public CountryCodePhoneResponse addCountryCodePhone(CountryCodePhoneRequest request) {
    CountryCodePhone countryCodePhone = CountryCodePhone.builder()
        .isoCode(request.getIsoCode())
        .country(request.getCountry())
        .countryTelephoneCodes(request.getCountryTelephoneCodes())
        .build();

    CountryCodePhone saved = repository.save(countryCodePhone);

    return CountryCodePhoneResponse.builder()
        .isoCode(saved.getIsoCode())
        .country(saved.getCountry())
        .countryTelephoneCodes(saved.getCountryTelephoneCodes())
        .build();
  }

  @Override
  public Optional<CountryCodePhoneResponse> findCountryCodeByIso(String isoCode) {
    return repository.findById(isoCode).map(countryCodePhone -> CountryCodePhoneResponse.builder()
        .isoCode(countryCodePhone.getIsoCode())
        .country(countryCodePhone.getCountry())
        .countryTelephoneCodes(countryCodePhone.getCountryTelephoneCodes())
        .build());
  }

  @Override
  public void deleteCountryPhone(String isoCode) {
    CountryCodePhone countryCodePhone = repository.findById(isoCode)
        .orElseThrow(() ->
            new EntityNotFoundException("Country code phone cannot be found"));

    repository.delete(countryCodePhone);
  }

  @Override
  public List<CountryCodePhoneResponse> findCountryCodes() {
    return repository.findCountryCodes();
  }
}
