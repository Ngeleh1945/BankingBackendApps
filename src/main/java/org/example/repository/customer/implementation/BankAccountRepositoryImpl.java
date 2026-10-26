package org.example.repository.customer.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.example.model.customer.BankAccount;
import org.example.repository.customer.BankAccountRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of BankAccountRepository for managing bank account data access.
 */
@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Saves or updates a bank account entity.
   *
   * @param bankAccount the bank account to save or update
   */
  @Override
  @Transactional
  public BankAccount save(BankAccount bankAccount) {
    if (bankAccount.getAccountId() == null) {
      entityManager.persist(bankAccount);
      return bankAccount;
    } else {
      return entityManager.merge(bankAccount);
    }
  }

  /**
   * Finds a bank account by its account number.
   *
   * @param accountNumber the account number to search for
   * @return an Optional containing the bank account if found, or empty otherwise
   */
  @Override
  public Optional<BankAccount> findByAccountNumber(String accountNumber) {
    BankAccount bankAccount = entityManager.find(BankAccount.class, accountNumber);
    return Optional.ofNullable(bankAccount);
  }
}
