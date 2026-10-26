package org.example.service.transaction;

import java.math.BigDecimal;

/**
 * Service interface for managing bank transactions.
 */
public interface TransactionService {
  /**
   * Transfers funds from a sender account to a receiver account.
   *
   * @param senderAccountNumber the account number of the sender
   * @param receiverAccountNumber the account number of the receiver
   * @param amount the amount to transfer
   */
  void transfer(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount);

  /**
   * Performs a cash withdrawal from a bank account.
   *
   * @param accountNumber the account number
   * @param amount the amount to withdraw
   */
  void cashWithdrawal(String accountNumber, BigDecimal amount);

  /**
   * Performs a cash deposit into a bank account.
   *
   * @param accountNumber the account number
   * @param amount the amount to deposit
   */
  void cashDeposit(String accountNumber, BigDecimal amount);

  /**
   * Deposits cash from a sender account to a receiver account.
   *
   * @param accountNumberSender the account number of the sender
   * @param accountNumberReceiver the account number of the receiver
   * @param amount the amount to deposit
   */
  void cashDepositTo(String accountNumberSender, String accountNumberReceiver, BigDecimal amount);
}