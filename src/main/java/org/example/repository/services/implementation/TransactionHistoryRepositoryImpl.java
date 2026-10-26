package org.example.repository.services.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.model.transaction.TransactionHistory;
import org.example.repository.services.TransactionHistoryRepository;
import org.springframework.stereotype.Repository;

/**
 * Implementation of Repository for transaction history.
 */
@Repository
public class TransactionHistoryRepositoryImpl implements TransactionHistoryRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  @Transactional
  public TransactionHistory save(TransactionHistory transactionHistory) {
    entityManager.persist(transactionHistory);
    return transactionHistory;
  }
}
