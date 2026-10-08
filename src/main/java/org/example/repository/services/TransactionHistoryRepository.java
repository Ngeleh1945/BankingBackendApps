package org.example.repository.services;

import org.example.model.transaction.TransactionHistory;

public interface TransactionHistoryRepository {
    void save(TransactionHistory transactionHistory);
}
