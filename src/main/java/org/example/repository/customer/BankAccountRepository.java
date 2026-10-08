package org.example.repository.customer;

import org.example.model.customer.BankAccount;

import java.util.Optional;

public interface BankAccountRepository {
    void save(BankAccount bankAccount);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
}
