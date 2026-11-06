package org.example.repository.managementbank;

import org.example.model.managementbank.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface of Repository for transaction history.
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
}
