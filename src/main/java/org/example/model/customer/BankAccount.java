package org.example.model.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a bank account entity containing account and balance details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_accounts")
public class BankAccount {
  @Id
  @Column(name = "account_number")
  private String accountNumber;
  @Column(name = "balance")
  private BigDecimal balance;
  @Column(name = "account_type")
  private String accountType;
  @Column(name = "account_id")
  private String accountId;
}
