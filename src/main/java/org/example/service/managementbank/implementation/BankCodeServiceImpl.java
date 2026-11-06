package org.example.service.managementbank.implementation;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.managementbank.BankCodeRequest;
import org.example.dto.managementbank.ShowBankCode;
import org.example.model.managementbank.BankCode;
import org.example.repository.managementbank.BankCodeRepository;
import org.example.service.managementbank.BankCodeService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing bank code configurations and financial institution data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankCodeServiceImpl implements BankCodeService {
  private final BankCodeRepository repository;

  @Override
  public ShowBankCode addBankCode(BankCodeRequest request) {
    BankCode banks = BankCode
        .builder()
        .codeBank(request.getCodeBank())
        .bankName(request.getBankName())
        .swiftCode(request.getSwiftCode())
        .build();

    BankCode saved = repository.save(banks);
    return ShowBankCode
        .builder()
        .bankName(saved.getBankName())
        .build();
  }

  @Override
  public Optional<ShowBankCode> findBankByCodeBank(String codeBank) {
    return repository
        .findById(codeBank)
        .map(banks -> ShowBankCode
            .builder()
            .bankName(banks.getBankName())
            .build());
  }

  @Override
  public void deleteBankCode(String codeBank) {
    BankCode banks = repository
        .findById(codeBank)
        .orElseThrow(() ->
            new EntityNotFoundException("Code bank cannot be found"));

    repository.delete(banks);
  }

  @Override
  public List<ShowBankCode> findAllBank() {
    return repository
        .findAllBankName()
        .stream()
        .map(banks -> ShowBankCode
            .builder()
            .bankName(banks.getBankName())
            .build())
        .toList();
  }
}
