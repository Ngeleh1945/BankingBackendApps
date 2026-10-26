package org.example.repository.services;

import org.example.model.transaction.TransactionHistory;

/**
 * Interface of Repository for transaction history.
 */
public interface TransactionHistoryRepository {
  /**
   * Saves a transaction history record.
   *
   * @param transactionHistory the transaction history entity to save
   * @return the saved transaction history instance
   */
  TransactionHistory save(TransactionHistory transactionHistory);
}
