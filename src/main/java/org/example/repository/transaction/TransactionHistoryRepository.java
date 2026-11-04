package org.example.repository.transaction;

import org.example.model.transaction.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface of Repository for transaction history.
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
}
