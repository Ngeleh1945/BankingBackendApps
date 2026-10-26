package org.example.repository.customer;

import java.util.Optional;
import org.example.model.customer.BankAccount;

/**
 * Interface BankAccount data.
 */
public interface BankAccountRepository {
  /**
   * Saves or updates a bank account entity.
   *
   * @param bankAccount the bank account to save or update
   * @return the saved bank account instance
   */
  BankAccount save(BankAccount bankAccount);

  /**
   * Finds a bank account by its account number.
   *
   * @param accountNumber the account number to search for
   * @return an Optional containing the bank account if found, or empty otherwise
   */
  Optional<BankAccount> findByAccountNumber(String accountNumber);
}
