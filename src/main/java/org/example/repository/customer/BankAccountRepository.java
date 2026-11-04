package org.example.repository.customer;

import java.util.Optional;
import org.example.model.customer.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Interface BankAccount for managing account bank data.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
