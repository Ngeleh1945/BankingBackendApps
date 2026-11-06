package org.example.service.managementbank;

import java.util.List;
import java.util.Optional;
import org.example.dto.managementbank.BankCodeRequest;
import org.example.dto.managementbank.ShowBankCode;

/**
 * Service interface for managing bank code configurations and financial institution data.
 */
public interface BankCodeService {

  /**
   * Adds a new bank code configuration into the system.
   *
   * @param request the {@link BankCodeRequest} containing bank code details to add
   * @return a {@link ShowBankCode} representing the newly created bank code entry
   */
  ShowBankCode addBankCode(BankCodeRequest request);

  /**
   * Retrieves a specific bank code configuration by its unique bank code.
   *
   * @param codeBank the unique bank code to search for
   * @return an {@link Optional} containing the found {@link ShowBankCode}, or empty if not found
   */
  Optional<ShowBankCode> findBankByCodeBank(String codeBank);

  /**
   * Deletes a bank code configuration from the system based on its unique bank code.
   *
   * @param codeBank the unique bank code of the entry to delete
   */
  void deleteBankCode(String codeBank);

  /**
   * Retrieves a list of all available bank code configurations.
   *
   * @return a {@link List} of {@link ShowBankCode} containing all bank entries
   */
  List<ShowBankCode> findAllBank();
}